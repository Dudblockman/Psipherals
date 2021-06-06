package com.dudblockman.psipherals.spell.operator.overrides;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.common.spell.operator.number.PieceOperatorSquareRoot;

public class OperatorAdaptiveSquareRoot extends OperatorAdaptiveSingleBase {

    public OperatorAdaptiveSquareRoot(Spell spell) {
        super(spell);
        orignalPiece = new PieceOperatorSquareRoot(spell);
    }

    @Override
    public double operation(double a) throws SpellRuntimeException {
        if (a < 0) {
            throw new SpellRuntimeException(SpellRuntimeException.NEGATIVE_NUMBER);
        }
        return Math.sqrt(a);
    }

}
