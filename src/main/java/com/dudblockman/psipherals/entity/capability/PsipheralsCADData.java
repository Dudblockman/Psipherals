package com.dudblockman.psipherals.entity.capability;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class PsipheralsCADData implements ICapabilityProvider, INBTSerializable<NBTTagCompound> {

    @CapabilityInject(PsipheralsCADData.class)
    Capability<PsipheralsCADData> CAPABILITY = null;

    private List<Byte> bytes = Lists.newArrayList();
    private boolean dirty;

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CAPABILITY;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
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
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();

        NBTTagByteArray memory = new NBTTagByteArray(bytes);

        compound.setTag("PsipheralsMemory", memory);

        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("PsipheralsMemory", Constants.NBT.TAG_LIST)) {
            byte[] newBytes = nbt.getByteArray("PsipheralsMemory");

            bytes = Lists.newArrayList();

            for (byte b : newBytes) {
                bytes.add(b);
            }
        }
    }
}
