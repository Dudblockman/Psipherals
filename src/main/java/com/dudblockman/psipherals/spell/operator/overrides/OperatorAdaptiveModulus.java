package com.dudblockman.psipherals.spell.operator.overrides;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.common.spell.operator.number.PieceOperatorModulus;

public class OperatorAdaptiveModulus extends OperatorAdaptiveDoubleBase {

    public OperatorAdaptiveModulus(Spell spell) {
        super(spell);
        defaultValue = 1D;
        orignalPiece = new PieceOperatorModulus(spell);
    }

    @Override
    public void initParams() {
        addParam(in1 = new ParamVectorNumber(SpellParam.GENERIC_NAME_NUMBER1, SpellParam.RED, false));
        addParam(in2 = new ParamVectorNumber(SpellParam.GENERIC_NAME_NUMBER2, SpellParam.GREEN, false));
    }

    public double operation(double a, double b) throws SpellRuntimeException {
        if (b == 0) {
            throw new SpellRuntimeException(SpellRuntimeException.DIVIDE_BY_ZERO);
        }
        return a % b;
    }

}
