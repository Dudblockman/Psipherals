package com.dudblockman.psipherals.entity.capability;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.LongNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PsipheralsCADData implements ICapabilityProvider, INBTSerializable<CompoundNBT> {


    @CapabilityInject(PsipheralsCADData.class)
    public static final Capability<PsipheralsCADData> CAPABILITY = null;

    private Long data = 0L;
    private boolean dirty;
    public final ItemStack cadStack;

    public PsipheralsCADData(ItemStack stack){
        this.cadStack = stack;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable Direction facing) {
        return capability == CAPABILITY;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        return capability == CAPABILITY ? CAPABILITY.cast(this) : null;
    }

    boolean hasData(ItemStack stack) {
        return stack.hasCapability(CAPABILITY, null);
    }

    PsipheralsCADData data(ItemStack stack) {
        return stack.getCapability(CAPABILITY, null);
    }

    public boolean isDirty() {
        return dirty;
    }

    public void markDirty(boolean isDirty) {
        dirty = isDirty;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT compound = new CompoundNBT();

        LongNBT memory = new LongNBT(data);

        compound.setTag("PsipheralsMemory", memory);

        return compound;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (nbt.hasKey("PsipheralsMemory", Constants.NBT.TAG_LIST)) {
            data = nbt.getLong("PsipheralsMemory");
        }
    }
}
