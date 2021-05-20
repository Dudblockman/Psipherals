package com.dudblockman.psipherals.crafting;

import com.dudblockman.psipherals.Psipherals;
import com.dudblockman.psipherals.block.tile.TilePsilon;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import vazkii.psi.api.internal.Vector3;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

public class InfusionCraftingHelper {
    public static final int MIN_RADIUS = 2;
    public static final int MAX_RADIUS = 7;
    private static final int MIN_RADIUS_SQ = MIN_RADIUS * MIN_RADIUS;
    private static final int MAX_RADIUS_SQ = MAX_RADIUS * MAX_RADIUS;
    private static final AxisAlignedBB SEARCH_SPACE = new AxisAlignedBB(-MAX_RADIUS,0,-MAX_RADIUS,1+MAX_RADIUS,1,1+MAX_RADIUS);

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
        Optional<PsilonInfusionRecipe> recipe = getRecipe(master, getInfusionProviders(master));
        if (!recipe.isPresent()) {
            return;
        }
        for (int i = 0; i < master.connectedPsilons.size(); i++) {
            TileEntity te = master.getWorld().getTileEntity(master.connectedPsilons.get(i));
            if (!(te instanceof TilePsilon)) {
                //Oh no this bad
                return;
            }
            ((TilePsilon) te).consumeItem();
            ((TilePsilon) te).sync();
        }
        master.mode = TilePsilon.InfusionState.CONSUMING;
        master.replaceItem(recipe.get().getRecipeOutput().copy());
        master.scheduleInfusionTick();

        master.disconnect(true);
    }

    public static InfusionError invokePsilon(TilePsilon target, Vector3 frequency) {
        World worldIn = target.getWorld();
        BlockPos pos = target.getPos();
        int value = target.activate(frequency);
        Set<ResourceLocation> tags = worldIn.getBlockState(pos.down()).getBlock().getTags();
        if (tags.contains(InfusionCraftingHelper.BASE_TAG)) {
            if (value == 15) {
                List<TilePsilon> entities = getInfusionProviders(target);
                InfusionCraftingHelper.InfusionError code = validateInfusionProviders(pos, entities);
                if (code == InfusionCraftingHelper.InfusionError.NONE) {
                    if (getRecipe(target, entities).isPresent()) {
                        target.connectToSlaves(entities);
                        stepInfusion(target);
                    }
                }
                return code;
            }
        }
        if (tags.contains(InfusionCraftingHelper.PROVIDER_TAG)) {
            if (target.isSlave()) {
                if (value == 15) {
                    stepInfusion(target);
                } else {
                    target.disconnect(true);
                    return InfusionError.INCORRECT_FREQUENCY;
                }
            }
        }
        return InfusionError.NONE;
    }

    public static Optional<PsilonInfusionRecipe> getRecipe(TilePsilon master, List<TilePsilon> entities) {
        RecipeWrapper inv = new RecipeWrapper(new ItemStackHandler(entities.size() + 1));
        inv.setInventorySlotContents(0, master.getHeldItem());
        for(int i = 0; i < entities.size(); i++) {
            inv.setInventorySlotContents(i+1, entities.get(i).getHeldItem());
        }
        Optional<PsilonInfusionRecipe> recipe = master.getWorld().getRecipeManager().getRecipe(CraftingRecipes.INFUSION_TYPE, inv, master.getWorld());
        /*if (recipe.isPresent()) {
            ItemStack outCopy = recipe.get().getRecipeOutput().copy();
            outCopy.setCount(stack.getCount());
            item.setItem(outCopy);
            did = true;
            MessageVisualEffect msg = new MessageVisualEffect(ICADColorizer.DEFAULT_SPELL_COLOR,
                    item.getPosX(), item.getPosY(), item.getPosZ(), item.getWidth(), item.getHeight(), item.getYOffset(),
                    MessageVisualEffect.TYPE_CRAFT);
            MessageRegister.HANDLER.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> item), msg);
        }*/
        return recipe;
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
