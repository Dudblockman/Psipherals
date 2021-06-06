package com.dudblockman.psipherals.spell.operator.overrides;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.common.spell.operator.number.PieceOperatorCeiling;

public class OperatorAdaptiveCeiling extends OperatorAdaptiveSingleBase {

    @Override
    public void initParams() {
        addParam(in1 = new ParamVectorNumber(SpellParam.GENERIC_NAME_NUMBER, SpellParam.GREEN, false));
    }

    public OperatorAdaptiveCeiling(Spell spell) {
        super(spell);
        orignalPiece = new PieceOperatorCeiling(spell);
    }

    @Override
    public double operation(double a) {
        return Math.ceil(a);
    }

}
