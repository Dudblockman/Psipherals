package com.dudblockman.psipherals.spell.operator.overrides;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.common.spell.operator.number.PieceOperatorCeiling;

public class OperatorAdaptiveCeiling extends OperatorAdaptiveSingleBase {

    public OperatorAdaptiveCeiling(Spell spell) {
        super(spell);
        orignalPiece = new PieceOperatorCeiling(spell);
    }

    @Override
    public void initParams() {
        addParam(in1 = new ParamVectorNumber(SpellParam.GENERIC_NAME_NUMBER, SpellParam.GREEN, false));
    }

    @Override
    public double operation(double a) {
        return Math.ceil(a);
    }

}
