package com.dudblockman.psipherals.mixins;

import com.dudblockman.psipherals.spell.other.PieceCADOverclock;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.spell.CompiledSpell;
import vazkii.psi.api.spell.EnumSpellStat;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.client.gui.GuiProgrammer;
import vazkii.psi.client.gui.widget.SpellCostsWidget;
import vazkii.psi.common.item.ItemCAD;

import java.util.Iterator;

@SuppressWarnings("UnresolvedMixinReference")
@Mixin(value = SpellCostsWidget.class)
public class SpellCostMixin {
    @Final
    @Shadow
    private GuiProgrammer parent;

    private static final ThreadLocal<EnumSpellStat> currentStat = new ThreadLocal<>();
    @Inject(
            remap = false,
            method = "lambda$renderButton$0",
            at = @At(
                    value = "INVOKE",
                    target = "Lvazkii/psi/api/spell/SpellMetadata;getStat(Lvazkii/psi/api/spell/EnumSpellStat;)I"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD,
            require = 1
    )
    public void getStat(MatrixStack ms, int mouseX, int mouseY, CompiledSpell compiledSpell, CallbackInfo ci, int i,
                        int statX, SpellMetadata meta, ItemStack cad, Iterator var9, EnumSpellStat stat) {
        currentStat.set(stat);
    }

    @ModifyArg(
            remap = false,
            method = "lambda$renderButton$0",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/FontRenderer;drawString(Lcom/mojang/blaze3d/matrix/MatrixStack;Ljava/lang/String;FFI)I"
            ),
            index = 1,
            require = 1
    )
    public String replace(String in) {
        if (parent.compileResult.left().isPresent()) {
            SpellMetadata meta = parent.compileResult.left().get().metadata;
            if (meta.getFlag(PieceCADOverclock.OVERCLOCK)) {
                ItemStack cad = PsiAPI.getPlayerCAD(parent.getMinecraft().player);
                int val = meta.getStat(currentStat.get());
                EnumCADStat cadStat = currentStat.get().getTarget();
                int cadVal = 0;
                if (cadStat == null) {
                    cadVal = -1;
                } else if (!cad.isEmpty()) {
                    ICAD cadItem = (ICAD) cad.getItem();
                    cadVal = cadItem.getStatValue(cad, cadStat);
                }
                String s = "" + val;
                switch (currentStat.get()) {
                    case COST:
                        int cost = ItemCAD.getRealCost(cad, ItemStack.EMPTY, meta.getStat(EnumSpellStat.COST));
                        int newCost = (int) (cost * PieceCADOverclock.DISCOUNT_MULTIPLIER);
                        int burnout = (int) (cost * PieceCADOverclock.BURNOUT_MULTIPLIER);
                        return s + " (" + newCost + "+\u00A7c" + burnout + "\u00A7r)";
                    case POTENCY:
                        s = s + "/" + (cadVal == -1 ? "\u221E" : (int) Math.ceil(cadVal * PieceCADOverclock.POTENCY_MULTIPLIER));
                        if (val <= (int) Math.ceil(cadVal * PieceCADOverclock.POTENCY_MULTIPLIER)) {
                            s = "\u00A7f" + s;
                        }
                        return s;
                }
            }
        }
        return in;
    }
}
