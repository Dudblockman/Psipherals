package com.dudblockman.psipherals.spell.operator.overrides;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.spell.operator.number.PieceOperatorMax;

public class OperatorAdaptiveMax extends OperatorAdaptiveTripleBase {

    public OperatorAdaptiveMax(Spell spell) {
        super(spell);
        defaultValue = Double.NEGATIVE_INFINITY;
        orignalPiece = new PieceOperatorMax(spell);
    }

    public double operation(double a, double b, double c) {
        return Math.max(a, Math.max(b, c));
    }

}
