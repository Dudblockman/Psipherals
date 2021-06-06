package com.dudblockman.psipherals.spell.operator.overrides;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.spell.operator.number.PieceOperatorAbsolute;

public class OperatorAdaptiveAbsolute extends OperatorAdaptiveSingleBase {

    public OperatorAdaptiveAbsolute(Spell spell) {
        super(spell);
        orignalPiece = new PieceOperatorAbsolute(spell);
    }

    @Override
    public double operation(double a) {
        return Math.abs(a);
    }

}
