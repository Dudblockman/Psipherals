package com.dudblockman.psipherals.mixins;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.dudblockman.psipherals.util.EventHandler.ejectRidingSpells;

@Mixin(FireworkRocketEntity.class)
public class FireworkSpellMixin {
    @Inject(
            method = "func_213893_k()V",
            at = @At(value = "HEAD")
    )
    public void onExplode(CallbackInfo ci) {
        ejectRidingSpells(((Entity) (Object) this));
    }
}
