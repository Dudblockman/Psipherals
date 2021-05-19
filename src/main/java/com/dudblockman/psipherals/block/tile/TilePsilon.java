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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.TickPriority;
import net.minecraftforge.registries.ObjectHolder;
import vazkii.psi.api.internal.Vector3;

public class TilePsilon extends TileEntity {
    @ObjectHolder(Psipherals.MODID + ":" + BlockNames.PSILON)
    public static TileEntityType<TilePsilon> TYPE;

    ItemStack stack = ItemStack.EMPTY;
    private int comparatorValue = 0;
    private long updateTime = 0;

    public TilePsilon() {
        super(TYPE);
    }

    public void onDestroyed(BlockState state, BlockPos pos) {
        if (!stack.isEmpty()) InventoryHelper.spawnItemStack(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack);
    }

    public ActionResultType onActivated(BlockState state, BlockPos pos, PlayerEntity player, Hand hand) {
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

    public void readPacketNBT(CompoundNBT tag) {
        stack = ItemStack.read(tag.getCompound("stack"));
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
        return tag;
    }

    public void sync() {
        markDirty();
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
        if (this.world != null && this.world.getGameTime() - updateTime < 20) {
            return comparatorValue;
        }
        return 0;
    }
    public void activate(Vector3 inputFrequency) {
        if (this.world == null) {
            return;
        }
        updateTime = this.world.getGameTime();
        comparatorValue = (int) Math.max(1, 15 - inputFrequency.mag());
        this.world.updateComparatorOutputLevel(this.pos, this.getBlockState().getBlock());
        this.world.getPendingBlockTicks().scheduleTick(this.pos,this.getBlockState().getBlock(),20, TickPriority.HIGH);
    }
}
