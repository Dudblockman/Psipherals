package com.dudblockman.psipherals.spell.operator.overrides;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.spell.operator.number.PieceOperatorSum;

public class OperatorAdaptiveSum extends OperatorAdaptiveTripleBase {

    public OperatorAdaptiveSum(Spell spell) {
        super(spell);
        orignalPiece = new PieceOperatorSum(spell);
    }

    public double operation(double a, double b, double c) {
        return a + b + c;
    }

}
