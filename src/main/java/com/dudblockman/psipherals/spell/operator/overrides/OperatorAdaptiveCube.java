package com.dudblockman.psipherals.spell.operator.overrides;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.spell.operator.number.PieceOperatorCube;

public class OperatorAdaptiveCube extends OperatorAdaptiveSingleBase {

    public OperatorAdaptiveCube(Spell spell) {
        super(spell);
        orignalPiece = new PieceOperatorCube(spell);
    }

    @Override
    public double operation(double a) {
        return a * a * a;
    }

}
