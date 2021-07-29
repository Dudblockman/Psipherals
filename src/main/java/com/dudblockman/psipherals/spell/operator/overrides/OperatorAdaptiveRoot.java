package com.dudblockman.psipherals.spell.operator.overrides;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.common.spell.operator.number.PieceOperatorModulus;

public class OperatorAdaptiveRoot extends OperatorAdaptiveDoubleBase {

    public OperatorAdaptiveRoot(Spell spell) {
        super(spell);
        defaultValue = 0D;
        orignalPiece = new PieceOperatorModulus(spell);
    }

    @Override
    public void initParams() {
        addParam(in1 = new ParamVectorNumber(SpellParam.GENERIC_NAME_NUMBER, SpellParam.GREEN, false));
        addParam(in2 = new ParamVectorNumber(SpellParam.GENERIC_NAME_ROOT, SpellParam.RED, false));
    }

    public double operation(double a, double b) throws SpellRuntimeException {
        if (a < 0 && b % 2 == 0) {
            throw new SpellRuntimeException(SpellRuntimeException.EVEN_ROOT_NEGATIVE_NUMBER);
        }
        return Math.pow(a, 1 / b);
    }

}
