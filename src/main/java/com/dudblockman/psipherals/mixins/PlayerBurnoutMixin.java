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
    private static final double recoveryRate = 0.05;

    public int burnoutPsi;

    @Shadow
    public int regenCooldown;
    @Shadow
    public int regen;

    @Shadow public abstract void deductPsi(int psi, int cd, boolean sync, boolean shatter);

    @Inject(
            method = "Lvazkii/psi/common/core/handler/PlayerDataHandler$PlayerData;tick()V",
            at = @At(
                    value = "TAIL"
            )
    )
    public void decreaseBurnout(CallbackInfo ci) {
        if (burnoutPsi > 0) {
            int deduction = (int) Math.ceil(burnoutPsi * recoveryRate);
            if (!(regenCooldown > 0) || deduction > regen) {
                burnoutPsi = Math.max(0, burnoutPsi - deduction);
                deductPsi(deduction,0,true,true);
            }
        }
    }
    @Inject(
            method = "writeToNBT(Lnet/minecraft/nbt/CompoundNBT;)V",
            at = @At(
                    value = "HEAD"
            )
    )
    public void saveNBT(CompoundNBT cmp, CallbackInfo ci) {
        cmp.putInt(TAG_BURNOUT_PSI, burnoutPsi);
    }
    @Inject(
            method = "readFromNBT(Lnet/minecraft/nbt/CompoundNBT;)V",
            at = @At(
                    value = "HEAD"
            )
    )
    public void loadNBT(CompoundNBT cmp, CallbackInfo ci) {
        burnoutPsi = cmp.getInt(TAG_BURNOUT_PSI);
    }
}
