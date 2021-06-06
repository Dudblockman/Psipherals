package com.dudblockman.psipherals.spell.operator.overrides;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.spell.operator.number.trig.PieceOperatorCos;

public class OperatorAdaptiveCos extends OperatorAdaptiveSingleBase {

    public OperatorAdaptiveCos(Spell spell) {
        super(spell);
        orignalPiece = new PieceOperatorCos(spell);
    }

    @Override
    public double operation(double a) {
        return Math.cos(a);
    }

}
