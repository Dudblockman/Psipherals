package com.dudblockman.psipherals.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.entity.EntitySpellProjectile;

@Mixin(EntitySpellProjectile.class)
public class ForkProjectileMixin {
    @Shadow
    public SpellContext context;

    @ModifyVariable(
            remap = false,
            method = "cast(Ljava/util/function/Consumer;)V",
            at = @At(
                    value = "JUMP",
                    opcode = 153,
                    shift = At.Shift.BY,
                    by = -3
            )
    )
    boolean skipFork (boolean s) {
        return context != null;
    }
}
