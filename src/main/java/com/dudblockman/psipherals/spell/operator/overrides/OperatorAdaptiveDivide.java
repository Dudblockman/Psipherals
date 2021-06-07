package com.dudblockman.psipherals.spell.operator.overrides;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.common.spell.operator.number.PieceOperatorDivide;

public class OperatorAdaptiveDivide extends OperatorAdaptiveTripleBase {

    public OperatorAdaptiveDivide(Spell spell) {
        super(spell);
        defaultValue = 1D;
        orignalPiece = new PieceOperatorDivide(spell);
    }

    @Override
    public void initParams() {
        addParam(in1 = new ParamVectorNumber(SpellParam.GENERIC_NAME_NUMBER1, SpellParam.RED, false));
        addParam(in2 = new ParamVectorNumber(SpellParam.GENERIC_NAME_NUMBER2, SpellParam.GREEN, false));
        addParam(in3 = new ParamVectorNumber(SpellParam.GENERIC_NAME_NUMBER3, SpellParam.YELLOW, true));
    }

    public double operation(double a, double b, double c) throws SpellRuntimeException {
        if (b == 0 || c == 0) {
            throw new SpellRuntimeException(SpellRuntimeException.DIVIDE_BY_ZERO);
        }
        return a / b / c;
    }

}
