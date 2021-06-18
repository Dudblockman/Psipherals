package com.dudblockman.psipherals.mixins;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import vazkii.psi.client.fx.FXSparkle;

@Mixin(FXSparkle.class)
public class ParticleFixHackMixin {
    @Inject(
            method = "<init>",
            at = @At("TAIL"),
            locals = LocalCapture.CAPTURE_FAILHARD

    )
    public void constructor(ClientWorld world, double x, double y, double z, float size, float red, float green, float blue, int m, double mx, double my, double mz, IAnimatedSprite sprite, CallbackInfo ci) {
        Particle particle = ((FXSparkle) (Object) this);
        double multiplier = (1 - Math.pow(0.9, m)) * 10;
        particle.setBoundingBox(particle.getBoundingBox().grow(mx * multiplier, my * multiplier, mz * multiplier));
    }
}
