package com.dudblockman.psipherals.mixins;

import com.dudblockman.psipherals.Psipherals;
import com.dudblockman.psipherals.spell.operator.overrides.OperatorAdaptiveBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellPiece;

import java.util.Set;

@Mixin(Spell.class)
public class RequiredSpellMixin {
    @Inject(
            method = "getPieceNamespaces()Ljava/util/Set;",
            at = @At(
                    value = "INVOKE",
                    target = "java/util/Set.add (Ljava/lang/Object;)Z"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD,
            remap = false
    )
    @SuppressWarnings("unchecked")
    void AddToSpell(CallbackInfoReturnable<Set> cir, Set temp, SpellPiece[][] var2, int var3, int var4, SpellPiece[] gridDatum, SpellPiece[] var6, int var7, int var8, SpellPiece spellPiece) {
        if(spellPiece instanceof OperatorAdaptiveBase) {
            if (((OperatorAdaptiveBase)spellPiece).orignalPiece != null && spellPiece.getEvaluationType() != ((OperatorAdaptiveBase)spellPiece).orignalPiece.getEvaluationType()) {
                temp.add(Psipherals.MODID);
            }
        }
    }

}
