package com.dudblockman.psipherals.spell.operator.overrides;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.spell.operator.number.PieceOperatorSquare;

public class OperatorAdaptiveSquare extends OperatorAdaptiveSingleBase {

    public OperatorAdaptiveSquare(Spell spell) {
        super(spell);
        orignalPiece = new PieceOperatorSquare(spell);
    }

    @Override
    public double operation(double a) {
        return a * a;
    }

}
