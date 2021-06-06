package com.dudblockman.psipherals.spell.operator.overrides;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.spell.operator.number.trig.PieceOperatorSin;

public class OperatorAdaptiveSin extends OperatorAdaptiveSingleBase {

    public OperatorAdaptiveSin(Spell spell) {
        super(spell);
        orignalPiece = new PieceOperatorSin(spell);
    }

    @Override
    public double operation(double a) {
        return Math.sin(a);
    }

}
