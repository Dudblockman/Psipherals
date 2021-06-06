package com.dudblockman.psipherals.spell.operator.overrides;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.common.spell.operator.number.PieceOperatorLog;

public class OperatorAdaptiveLog extends OperatorAdaptiveDoubleBase {

    public OperatorAdaptiveLog(Spell spell) {
        super(spell);
        defaultValue = 10D;
        orignalPiece = new PieceOperatorLog(spell);
    }

    @Override
    public void initParams() {
        addParam(in1 = new ParamVectorNumber(SpellParam.GENERIC_NAME_TARGET, SpellParam.BLUE, false));
        addParam(in2 = new ParamVectorNumber(SpellParam.GENERIC_NAME_BASE, SpellParam.RED, true));
    }

    public double operation (double a, double b) throws SpellRuntimeException {
        if (a < 0) {
            throw new SpellRuntimeException(SpellRuntimeException.NEGATIVE_NUMBER);
        }

        double logNum = Math.log10(a);

        if (b < 0) {
            throw new SpellRuntimeException(SpellRuntimeException.NEGATIVE_NUMBER);
        }

        logNum /= Math.log10(b);

        return logNum;
    }

}
