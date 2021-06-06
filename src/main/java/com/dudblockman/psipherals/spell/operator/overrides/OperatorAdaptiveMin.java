package com.dudblockman.psipherals.spell.operator.overrides;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.spell.operator.number.PieceOperatorMin;

public class OperatorAdaptiveMin extends OperatorAdaptiveTripleBase {

    public OperatorAdaptiveMin(Spell spell) {
        super(spell);
        defaultValue = Double.MAX_VALUE;
        orignalPiece = new PieceOperatorMin(spell);
    }

    public double operation (double a, double b, double c) {
        return Math.min(a,Math.min(b,c));
    }

}
