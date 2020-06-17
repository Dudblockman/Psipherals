package com.dudblockman.psipherals.entity.capability;

import com.dudblockman.psipherals.util.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.ISpellImmune;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ArrowSpellImmuneCapability implements ISpellImmune, ICapabilityProvider {

    public final Entity entity;

    public ArrowSpellImmuneCapability(Entity entity){
        this.entity = entity;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == PsiAPI.SPELL_IMMUNE_CAPABILITY)
            return entity.getTags().contains(EventHandler.TAG_SPELLIMMUNE) ? PsiAPI.SPELL_IMMUNE_CAPABILITY : null ;
        return null;
    }

    @Override
    public boolean isImmune() {
        return false;
    }

}