package com.dudblockman.psipherals.block.tile;

import com.dudblockman.psipherals.Psipherals;
import com.dudblockman.psipherals.util.libs.BlockNames;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.TickPriority;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.psi.api.internal.Vector3;

import java.util.ArrayList;
import java.util.List;

public class TilePsilon extends TileEntity implements IPsilonInfusionProvider {
    @ObjectHolder(Psipherals.MODID + ":" + BlockNames.PSILON)
    public static TileEntityType<TilePsilon> TYPE;

    public static final int ACTIVATION_TIME = 30;
    public static final int VECTOR_DOMAIN = 7;

    public InfusionState mode = InfusionState.OFF;

    ItemStack stack = ItemStack.EMPTY;
    private int comparatorValue = 0;
    private long updateTime = 0;

    public List<BlockPos> connectedPsilons = new ArrayList<>();

    public TilePsilon() {
        super(TYPE);
    }

    public void onDestroyed(BlockState state, BlockPos pos) {
        disconnect(true);
        if (!stack.isEmpty()) InventoryHelper.spawnItemStack(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
    }

    public ActionResultType onActivated(BlockState state, BlockPos pos, PlayerEntity player, Hand hand) {
        if (this.world != null && this.isSlave()) {
            TilePsilon master = this.getMaster();
            return ActionResultType.PASS;
        }
        if (this.mode != InfusionState.OFF) {
            return ActionResultType.PASS;
        }
        this.disconnect(true);
        if (hand == Hand.MAIN_HAND) {
            if (player.getHeldItem(hand).isEmpty() && !stack.isEmpty()) {
                player.addItemStackToInventory(stack);
                stack = ItemStack.EMPTY;
                if (!world.isRemote) sync();
                return ActionResultType.SUCCESS;
            }
            else if (!player.getHeldItem(hand).isEmpty() && stack.isEmpty()) {
                stack = player.getHeldItem(hand).copy();
                stack.setCount(1);
                player.getHeldItem(hand).shrink(1);
                if (player.getHeldItem(hand).isEmpty()) player.setHeldItem(hand, ItemStack.EMPTY);
                if (!world.isRemote) sync();
                return ActionResultType.SUCCESS;
            }
        }

        return ActionResultType.PASS;
    }

    public boolean isSlave() {
        return this.connectedPsilons != null && this.connectedPsilons.size() == 1;
    }

    public boolean isMaster() {
        return this.connectedPsilons != null && this.connectedPsilons.size() > 1;
    }

    public TilePsilon getMaster() {
        if (!isSlave() || this.world == null) {
            return null;
        }
        TileEntity te = this.world.getTileEntity(this.connectedPsilons.get(0));
        if (te instanceof TilePsilon) {
            return (TilePsilon) te;
        }
        return null;
    }

    public float getCircleActivation(float partialTicks) {
        if (this.mode == InfusionState.LIT || this.mode == InfusionState.CONSUMING) {
            return Math.min(1,Math.max((this.updateTime + 30 - (this.world.getGameTime() + partialTicks))/10, 0));
        }
        return Math.min((this.world.getGameTime() - this.updateTime + partialTicks)/10, 1);
    }

    private void connectToMaster(TilePsilon master) {
        this.connectedPsilons.add(master.getPos());
        sync();
    }

    public void connectToSlaves(List<TilePsilon> slaves) {
        disconnect(true);
        for (TilePsilon slave : slaves) {
            this.connectedPsilons.add(slave.getPos());
            slave.connectToMaster(this);
        }
        this.connectedPsilons.sort((BlockPos a, BlockPos b)->{
            double al = this.getFrequency(a).mag();
            double bl = this.getFrequency(b).mag();
            if (al == bl)
                return 0;
            return al > bl ? 1 : -1;
        });

        sync();
    }

    public void disconnect(boolean recursive) {
        this.mode = InfusionState.OFF;
        if (this.connectedPsilons == null || this.connectedPsilons.size() == 0) {
            this.connectedPsilons = new ArrayList<>();
            return;
        }
        if (recursive) {
            List<BlockPos> copy = connectedPsilons;
            this.connectedPsilons = new ArrayList<>();
            for (BlockPos pos : copy) {
                assert this.world != null;
                TileEntity tile = this.world.getTileEntity(pos);
                if (tile instanceof TilePsilon && ((TilePsilon) tile).connectedPsilons.size() > 0) {
                    ((TilePsilon) tile).disconnect(true);
                }
            }
        } else {
            this.connectedPsilons = new ArrayList<>();
        }
        sync();
    }

    public void readPacketNBT(CompoundNBT tag) {
        stack = ItemStack.read(tag.getCompound("stack"));
        long[] positions = tag.getLongArray("connectedPsilons");
        this.connectedPsilons = new ArrayList<>();
        for (long position : positions) {
            connectedPsilons.add(BlockPos.fromLong(position));
        }
        updateTime = tag.getLong("lastActivation");
        mode = InfusionState.values()[tag.getInt("infusionState")];
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
        this.readPacketNBT(tag);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag = super.write(tag);
        tag.put("stack", stack.write(new CompoundNBT()));
        if (connectedPsilons != null) {
            ArrayList<Long> positions = new ArrayList<>();
            for (BlockPos position : connectedPsilons) {
                positions.add(position.toLong());
            }
            tag.putLongArray("connectedPsilons", positions);
        }
        tag.putLong("lastActivation", updateTime);
        tag.putInt("infusionState",mode.ordinal());
        return tag;
    }


    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(getPos(), 0, write(new CompoundNBT()));
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.readPacketNBT(pkt.getNbtCompound());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return write(new CompoundNBT());
    }

    public int getComparatorValue() {
        if (this.world != null && this.world.getGameTime() - updateTime < ACTIVATION_TIME) {
            return comparatorValue;
        }
        return 0;
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return TileEntity.INFINITE_EXTENT_AABB;
    }

    public  Vector3 getFrequency(BlockPos pos) {
        int domain = 1 + VECTOR_DOMAIN * 2;
        long i = MathHelper.getPositionRandom(pos);
        i ^= ((ServerWorld) world).getSeed();
        i %= Math.pow(domain, 3);
        int x = (int) Math.floorMod(i, domain) - VECTOR_DOMAIN;
        int y = (int) Math.floorMod(i / domain, domain) - VECTOR_DOMAIN;
        int z = (int) Math.floorMod(i / domain / domain, domain) - VECTOR_DOMAIN;
        return new Vector3(x, y, z);
    }
    @Override
    public int activate(Vector3 inputFrequency) {
        comparatorValue = 1;
        if (this.world == null || !(this.world instanceof ServerWorld)) {
            return 0;
        }
        updateTime = this.world.getGameTime();

        Vector3 resonantFrequency = new Vector3();

        comparatorValue = (int) Math.max(1, 15 - (inputFrequency.sub(resonantFrequency)).mag());

        this.world.updateComparatorOutputLevel(this.pos, this.getBlockState().getBlock());
        this.scheduleOffTick();

        return comparatorValue;
    }

    @Override
    public ItemStack getHeldItem() {
        return stack;
    }

    @Override
    public void consumeItem() {
        stack = ItemStack.EMPTY;
        sync();
    }

    @Override
    public void replaceItem(ItemStack newStack) {
        this.stack = newStack;
        sync();
    }

    public void scheduleInfusionTick() {
        if (this.world == null || !this.isMaster() || this.mode != InfusionState.READY) {
            return;
        }
        this.updateTime = world.getGameTime();
        this.world.getPendingBlockTicks().scheduleTick(this.pos,this.getBlockState().getBlock(), 30, TickPriority.HIGH);
    }

    public void scheduleOffTick() {
        if (this.world == null) {
            return;
        }
        int time = (int) Math.max(0,updateTime - this.world.getGameTime() + ACTIVATION_TIME);
        this.world.getPendingBlockTicks().scheduleTick(this.pos,this.getBlockState().getBlock(),time, TickPriority.HIGH);

    }

    public void sync() {
        markDirty();
        this.world.notifyBlockUpdate(this.pos, this.getBlockState(), this.getBlockState(), 0);
    }

    public enum InfusionState {
        OFF,
        READY,
        LIT,
        CONSUMING
    }
}
