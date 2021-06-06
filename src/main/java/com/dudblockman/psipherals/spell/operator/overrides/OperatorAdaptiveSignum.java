package com.dudblockman.psipherals.spell.operator.overrides;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.spell.operator.number.PieceOperatorSignum;

public class OperatorAdaptiveSignum extends OperatorAdaptiveSingleBase {

    public OperatorAdaptiveSignum(Spell spell) {
        super(spell);
        orignalPiece = new PieceOperatorSignum(spell);
    }

    @Override
    public double operation(double a) {
        return Math.signum(a);
    }

}
