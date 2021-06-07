package com.dudblockman.psipherals.spell.operator.overrides;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.common.spell.operator.number.PieceOperatorSubtract;

public class OperatorAdaptiveSubtract extends OperatorAdaptiveTripleBase {

    public OperatorAdaptiveSubtract(Spell spell) {
        super(spell);
        orignalPiece = new PieceOperatorSubtract(spell);
    }

    @Override
    public void initParams() {
        addParam(in1 = new ParamVectorNumber(SpellParam.GENERIC_NAME_NUMBER1, SpellParam.RED, false));
        addParam(in2 = new ParamVectorNumber(SpellParam.GENERIC_NAME_NUMBER2, SpellParam.GREEN, false));
        addParam(in3 = new ParamVectorNumber(SpellParam.GENERIC_NAME_NUMBER3, SpellParam.GREEN, true));
    }

    public double operation(double a, double b, double c) {
        return a - b - c;
    }

}
