package com.dudblockman.psipherals.spell.trick;

import net.minecraft.item.ItemStack;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickSaveBinary extends PieceTrick {

    public static final String KEY_BINARY_LOCKED = "psipherals:BinaryLocked";

    SpellParam number;
    SpellParam target;

    public PieceTrickSaveBinary(Spell spell){
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(number = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER, SpellParam.BLUE, false, true));
        addParam(target = new ParamVector(SpellParam.GENERIC_NAME_TARGET, SpellParam.RED, false, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        meta.addStat(EnumSpellStat.COMPLEXITY, 1);

        Double numberVal = this.<Double>getParamEvaluation(number);
        if(numberVal == null || numberVal <= 0 || numberVal != numberVal.intValue())
            throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_INTEGER, x, y);

        meta.addStat(EnumSpellStat.POTENCY, numberVal.intValue() * 8);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Double numberVal = this.<Double>getParamValue(context, number);
        Vector3 targetVal = this.getParamValue(context, target);

        int n = numberVal.intValue() - 1;

        if(context.customData.containsKey(KEY_BINARY_LOCKED + n))
            return null;

        ItemStack cadStack = PsiAPI.getPlayerCAD(context.caster);
        if(cadStack == null || !(cadStack.getItem() instanceof ICAD))
            throw new SpellRuntimeException(SpellRuntimeException.NO_CAD);
        ICAD cad = (ICAD) cadStack.getItem();

        //cad.setStoredVector(cadStack, n, targetVal);

        context.customData.put(KEY_BINARY_LOCKED + n, 0);

        return null;
    }
}
