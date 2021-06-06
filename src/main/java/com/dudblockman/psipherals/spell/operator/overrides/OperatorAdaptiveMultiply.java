package com.dudblockman.psipherals.spell.operator.overrides;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.spell.operator.number.PieceOperatorMultiply;

public class OperatorAdaptiveMultiply extends OperatorAdaptiveTripleBase {

    public OperatorAdaptiveMultiply(Spell spell) {
        super(spell);
        defaultValue = 1D;
        orignalPiece = new PieceOperatorMultiply(spell);
    }

    public double operation (double a, double b, double c) {
        return a * b * c;
    }

}
