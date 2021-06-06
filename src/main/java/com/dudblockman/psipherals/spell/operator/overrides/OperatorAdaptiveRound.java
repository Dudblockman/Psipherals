package com.dudblockman.psipherals.spell.operator.overrides;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.common.spell.operator.number.PieceOperatorRound;

public class OperatorAdaptiveRound extends OperatorAdaptiveSingleBase {

    @Override
    public void initParams() {
        addParam(in1 = new ParamVectorNumber(SpellParam.GENERIC_NAME_NUMBER, SpellParam.GREEN, false));
    }

    public OperatorAdaptiveRound(Spell spell) {
        super(spell);
        orignalPiece = new PieceOperatorRound(spell);
    }

    @Override
    public double operation(double a) {
        return Math.round(a);
    }

}
