package com.dudblockman.psipherals.spell.selector;

import com.dudblockman.psipherals.spell.trick.PieceTrickSaveBinary;
import vazkii.psi.api.spell.Spell;

import net.minecraft.item.ItemStack;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceSelector;

public class PieceSelectorSavedBinary extends PieceSelector {

    SpellParam number;

    public PieceSelectorSavedBinary(Spell spell){
        super(spell);
    }
    @Override
    public void initParams() {
        addParam(number = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER, SpellParam.BLUE, false, true));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);

        Double numberVal = this.<Double>getParamEvaluation(number);
        if(numberVal == null || numberVal <= 0 || numberVal != numberVal.intValue())
            throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_INTEGER, x, y);

        meta.addStat(EnumSpellStat.POTENCY, numberVal.intValue() * 6);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Double numberVal = this.<Double>getParamValue(context, number);

        int n = numberVal.intValue() - 1;
        if(context.customData.containsKey(PieceTrickSaveBinary.KEY_BINARY_LOCKED + n))
            throw new SpellRuntimeException(SpellRuntimeException.LOCKED_MEMORY);

        ItemStack cadStack = PsiAPI.getPlayerCAD(context.caster);
        if(cadStack == null || !(cadStack.getItem() instanceof ICAD))
            throw new SpellRuntimeException(SpellRuntimeException.NO_CAD);
        ICAD cad = (ICAD) cadStack.getItem();
        return cad.getStoredVector(cadStack, n);
    }

    @Override
    public Class<?> getEvaluationType() {
        return Vector3.class;
    }
}
