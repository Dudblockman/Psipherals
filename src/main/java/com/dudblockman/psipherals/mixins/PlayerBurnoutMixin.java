package com.dudblockman.psipherals.mixins;

import net.minecraft.nbt.CompoundNBT;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.psi.common.core.handler.PlayerDataHandler;

@Mixin(PlayerDataHandler.PlayerData.class)
public abstract class PlayerBurnoutMixin {
    private static final String TAG_BURNOUT_PSI = "psipherals.burnoutPsi";

    private static final int maxBurnout = 10000;
    private static final double recoveryRate = 0.01;

    public int burnoutPsi;

    @Shadow
    public int regenCooldown;
    @Shadow
    public int regen;

    @Shadow public abstract void deductPsi(int psi, int cd, boolean sync);

    @Shadow public int availablePsi;

    @Inject(
            remap = false,
            method = "Lvazkii/psi/common/core/handler/PlayerDataHandler$PlayerData;tick()V",
            at = @At(
                    value = "HEAD"
            )
    )
    public void decreaseBurnout(CallbackInfo ci) {
        if (burnoutPsi > 0) {
            int deduction = (int) Math.ceil(burnoutPsi * recoveryRate);
            if (!(regenCooldown > 0) || deduction > regen) {
                if (availablePsi - deduction < 0) {
                    deduction *= 10; // For 10 ticks worth of draw damage to compensate for i-frames
                    deduction += 125; // Ensure at least 1/2 heart of damage
                }
                burnoutPsi = Math.max(0, burnoutPsi - deduction);
                deductPsi(deduction+4,0,true);
            }
        }
    }
    @Inject(
            remap = false,
            method = "writeToNBT(Lnet/minecraft/nbt/CompoundNBT;)V",
            at = @At(
                    value = "HEAD"
            )
    )
    public void saveNBT(CompoundNBT cmp, CallbackInfo ci) {
        cmp.putInt(TAG_BURNOUT_PSI, burnoutPsi);
    }
    @Inject(
            remap = false,
            method = "readFromNBT(Lnet/minecraft/nbt/CompoundNBT;)V",
            at = @At(
                    value = "HEAD"
            )
    )
    public void loadNBT(CompoundNBT cmp, CallbackInfo ci) {
        burnoutPsi = cmp.getInt(TAG_BURNOUT_PSI);
    }
}