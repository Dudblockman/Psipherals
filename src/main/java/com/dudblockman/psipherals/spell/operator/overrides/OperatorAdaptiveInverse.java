package com.dudblockman.psipherals.spell.operator.overrides;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.common.spell.operator.number.PieceOperatorInverse;

public class OperatorAdaptiveInverse extends OperatorAdaptiveSingleBase {

    public OperatorAdaptiveInverse(Spell spell) {
        super(spell);
        orignalPiece = new PieceOperatorInverse(spell);
    }

    @Override
    public double operation(double a) throws SpellRuntimeException {
        if (a == 0) {
            throw new SpellRuntimeException(SpellRuntimeException.DIVIDE_BY_ZERO);
        }
        return Math.abs(a);
    }

}
