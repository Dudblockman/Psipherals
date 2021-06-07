package com.dudblockman.psipherals.spell.operator.overrides;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.common.spell.operator.number.PieceOperatorPower;

public class OperatorAdaptivePower extends OperatorAdaptiveDoubleBase {

    public OperatorAdaptivePower(Spell spell) {
        super(spell);
        defaultValue = 1D;
        orignalPiece = new PieceOperatorPower(spell);
    }

    @Override
    public void initParams() {
        addParam(in1 = new ParamVectorNumber(SpellParam.GENERIC_NAME_BASE, SpellParam.GREEN, false));
        addParam(in2 = new ParamVectorNumber(SpellParam.GENERIC_NAME_POWER, SpellParam.RED, false));
    }

    public double operation(double a, double b) {
        return Math.pow(a, b);
    }

}
