package com.dudblockman.psipherals.entity.capability;

import com.dudblockman.psipherals.util.EventHandler;
import com.teamwizardry.librarianlib.features.helpers.NBTHelper;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import vazkii.psi.api.spell.ISpellImmune;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ArrowSpellImmuneCapability implements ISpellImmune, ICapabilityProvider {

    public final Entity entity;

    public ArrowSpellImmuneCapability(Entity entity){
        this.entity = entity;
    }


    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == ISpellImmune.CAPABILITY)
            return NBTHelper.hasKey(entity.getEntityData(), EventHandler.TAG_SPELLIMMUNE);
        return false;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == ISpellImmune.CAPABILITY)
            return NBTHelper.hasKey(entity.getEntityData(), EventHandler.TAG_SPELLIMMUNE) ? ISpellImmune.CAPABILITY.cast(this) : null ;
        return null;
    }

    @Override
    public boolean isImmune() {
        return true;
    }
}