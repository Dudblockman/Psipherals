package com.dudblockman.psipherals.util;

import com.dudblockman.psipherals.Psipherals;
import com.dudblockman.psipherals.block.tile.TilePsilon;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunk;
import vazkii.psi.api.internal.Vector3;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class InfusionCrafting {
    public static final int MIN_RADIUS = 2;
    public static final int MAX_RADIUS = 7;
    private static final int MIN_RADIUS_SQ = MIN_RADIUS * MIN_RADIUS;
    private static final int MAX_RADIUS_SQ = MAX_RADIUS * MAX_RADIUS;
    private static final AxisAlignedBB SEARCH_SPACE = new AxisAlignedBB(-MAX_RADIUS,-1,-MAX_RADIUS,1+MAX_RADIUS,2,1+MAX_RADIUS);

    public static final ResourceLocation BASE_TAG = Psipherals.location("psilon/base");
    public static final ResourceLocation PROVIDER_TAG = Psipherals.location("psilon/provider");

    public enum InfusionError {
        NONE,
        MULTIPLE_RADIUS,
        UNBALANCED_PLACEMENT,
        INCORRECT_FREQUENCY,
        PREMATURE_ACTIVATION
    }

    public static void stepInfusion(TilePsilon psilon) {
        TilePsilon master = psilon;
        if (psilon.isSlave()) {
            master = psilon.getMaster();
        }
        if (!master.isMaster()) {
            return;
        }
        for (int i = 0; i < master.connectedPsilons.size(); i++) {
            TileEntity te = master.getWorld().getTileEntity(master.connectedPsilons.get(i));
            if (!(te instanceof TilePsilon)) {
                //Oh no this bad
                return;
            }
            switch (((TilePsilon) te).mode) {
                case OFF:
                    ((TilePsilon) te).mode = TilePsilon.InfusionState.READY;
                    ((TilePsilon) te).sync();
                    return;
                case READY:
                    if (te.equals(psilon)) {
                        ((TilePsilon) te).mode = TilePsilon.InfusionState.LIT;
                        ((TilePsilon) te).sync();
                        if (i == master.connectedPsilons.size()-1) {
                            master.mode = TilePsilon.InfusionState.READY;
                            master.scheduleInfusionTick();
                        }
                    } else {
                        master.disconnect(true);
                    }
            }
        }
    }

    public static void ActivateInfusion(TilePsilon master) {
        if (!master.isMaster()) {
            return;
        }
        for (int i = 0; i < master.connectedPsilons.size(); i++) {
            TileEntity te = master.getWorld().getTileEntity(master.connectedPsilons.get(i));
            if (!(te instanceof TilePsilon)) {
                //Oh no this bad
                return;
            }
            ((TilePsilon) te).mode = TilePsilon.InfusionState.CONSUMING;
            ((TilePsilon) te).sync();
        }
        master.scheduleInfusionTick();
        master.mode = TilePsilon.InfusionState.LIT;
        master.sync();
    }

    public static void infusionCraft(TilePsilon master) {
        for (int i = 0; i < master.connectedPsilons.size(); i++) {
            TileEntity te = master.getWorld().getTileEntity(master.connectedPsilons.get(i));
            if (!(te instanceof TilePsilon)) {
                //Oh no this bad
                return;
            }
            master.mode = TilePsilon.InfusionState.CONSUMING;
            master.replaceItem(new ItemStack(Items.BONE));
            master.scheduleInfusionTick();
        }

        master.disconnect(true);
    }

    public static InfusionError invokePsilon(TilePsilon target, Vector3 frequency) {
        World worldIn = target.getWorld();
        BlockPos pos = target.getPos();
        int value = target.activate(frequency);
        Set<ResourceLocation> tags = worldIn.getBlockState(pos.down()).getBlock().getTags();
        if (tags.contains(InfusionCrafting.BASE_TAG)) {
            if (value == 15) {
                List<TilePsilon> entities = getInfusionProviders(target);
                InfusionCrafting.InfusionError code = validateInfusionProviders(pos, entities);
                if (code == InfusionCrafting.InfusionError.NONE) {
                    target.connectToSlaves(entities);
                    stepInfusion(target);
                }
                return code;
            }
        }
        if (tags.contains(InfusionCrafting.PROVIDER_TAG)) {
            if (target.isSlave()) {
                if (value == 15) {
                    stepInfusion(target);
                    System.out.println("YAAAAAAAAAAAAAAAAAAAAAAAY");
                } else {
                    target.disconnect(true);
                    return InfusionError.INCORRECT_FREQUENCY;
                }
            }
        }
        return InfusionError.NONE;
    }

    public static boolean hasValidRecipe(TilePsilon master, List<TilePsilon> entities) {
        //TODO get racipe here somehow
        return true;
    }

    public static InfusionError validateInfusionProviders(BlockPos origin, List<TilePsilon> pylons) {
        List<Double> distances = new ArrayList<>(pylons.size());
        Vector3 originVec = Vector3.fromBlockPos(origin);
        Vector3 centroid = new Vector3(0,0,0);
        double averageDistance = 0;
        for (TilePsilon pylon : pylons) {
            double distance = Vector3.fromTileEntity(pylon).sub(originVec).mag();
            centroid = centroid.add(Vector3.fromBlockPos(pylon.getPos()));
            distances.add(distance);
            averageDistance += distance;
        }
        centroid = centroid.multiply(1.0 / pylons.size());
        averageDistance /= pylons.size();
        double variance = 0;
        for (double d : distances) {
            variance += Math.pow(d - averageDistance,2);
        }
        variance /= pylons.size();
        if (Math.sqrt(variance) > 0.25) {
            return InfusionError.MULTIPLE_RADIUS;
        }
        if (centroid.sub(originVec).mag() > 0.5) {
            return InfusionError.UNBALANCED_PLACEMENT;
        }
        return InfusionError.NONE;
    }

    public static List<TilePsilon> getInfusionProviders(TilePsilon master) {
        World worldIn = master.getWorld();
        BlockPos origin = master.getPos();
        assert worldIn != null;
        return getTileEntitiesWithinAABB(worldIn, TilePsilon.class, SEARCH_SPACE.offset(origin),(te) -> {
            BlockPos pos = te.getPos();
            int distanceSq = (int) pos.distanceSq(origin.getX(), origin.getY(), origin.getZ(),false);
            if (distanceSq >= MIN_RADIUS_SQ && distanceSq <= MAX_RADIUS_SQ) {
                return worldIn.getBlockState(pos.down()).getBlock().getTags().contains(PROVIDER_TAG);
            }
            return false;
        });
    }


    public static <T extends TileEntity> List<T> getTileEntitiesWithinAABB(World world, Class<? extends T> type, AxisAlignedBB aabb) {
        return getTileEntitiesWithinAABB(world, type, aabb, x -> true);
    }
    public static <T extends TileEntity> List<T> getTileEntitiesWithinAABB(World world, Class<? extends T> type, AxisAlignedBB aabb, Predicate<? super T> predicate) {
        List<T> tileList = new ArrayList<>();
        for (int i = (int)Math.floor(aabb.minX); i < (int)Math.ceil(aabb.maxX) + 16; i += 16) {
            for (int j = (int)Math.floor(aabb.minZ); j < (int)Math.ceil(aabb.maxZ) + 16; j += 16) {
                IChunk c = world.getChunk(new BlockPos(i, 0, j));
                Set<BlockPos> tiles = c.getTileEntitiesPos();
                for (BlockPos p : tiles) {
                    if (aabb.contains(p.getX() + 0.5, p.getY() + 0.5, p.getZ() + 0.5)) {
                        TileEntity t = world.getTileEntity(p);
                        if (type.isInstance(t) && predicate.test((T) t)) {
                            tileList.add((T) t);
                        }
                    }
                }
            }
        }
        return tileList;
    }
}
