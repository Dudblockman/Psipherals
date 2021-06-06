package com.dudblockman.psipherals.spell.operator.overrides;

import net.minecraft.client.Minecraft;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.common.spell.operator.number.PieceOperatorModulus;

public class OperatorAdaptiveRandom extends OperatorAdaptiveDoubleBase {

    public OperatorAdaptiveRandom(Spell spell) {
        super(spell);
        defaultValue = 0D;
        orignalPiece = new PieceOperatorModulus(spell);
    }

    @Override
    public void initParams() {
        addParam(in1 = new ParamVectorNumber(SpellParam.GENERIC_NAME_MAX, SpellParam.BLUE, false));
        addParam(in2 = new ParamVectorNumber(SpellParam.GENERIC_NAME_MIN, SpellParam.RED, true));
    }

    public double operation (double a, double b) throws SpellRuntimeException {
        int maxVal = (int) a;
        int minVal = (int) b;
        if (maxVal - minVal <= 0) {
            throw new SpellRuntimeException(SpellRuntimeException.NEGATIVE_NUMBER);
        }
        assert Minecraft.getInstance().world != null;
        return (double) Minecraft.getInstance().world.rand.nextInt(maxVal - minVal) + minVal;
    }

}
