package com.dudblockman.psipherals.spell.operator.overrides;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.common.spell.operator.number.trig.PieceOperatorAcos;

public class OperatorAdaptiveAcos extends OperatorAdaptiveSingleBase {

    public OperatorAdaptiveAcos(Spell spell) {
        super(spell);
        orignalPiece = new PieceOperatorAcos(spell);
    }

    @Override
    public double operation(double a) throws SpellRuntimeException {
        if (a < -1 || a > 1) {
            throw new SpellRuntimeException("psi.spellerror.outsidetrigdomain");
        }
        return Math.acos(a);
    }

}
