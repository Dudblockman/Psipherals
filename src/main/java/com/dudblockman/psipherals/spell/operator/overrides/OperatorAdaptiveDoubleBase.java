package com.dudblockman.psipherals.spell.operator.overrides;

import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;

public abstract class OperatorAdaptiveDoubleBase extends OperatorAdaptiveBase {

    SpellParam<VectorOrNumber> in1;
    SpellParam<VectorOrNumber> in2;

    public OperatorAdaptiveDoubleBase(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(in1 = new ParamVectorNumber(SpellParam.GENERIC_NAME_NUMBER1, SpellParam.GREEN, false));
        addParam(in2 = new ParamVectorNumber(SpellParam.GENERIC_NAME_NUMBER2, SpellParam.GREEN, false));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Object d1 = this.getParamValue(context, in1);
        Object d2 = this.getParamValue(context, in2);
        if (d1 instanceof Vector3 || d2 instanceof Vector3) {
            return operation(vectorize(d1), vectorize(d2));
        } else {
            double a = ((Number) d1).doubleValue();
            double b = ((Number) d2).doubleValue();
            return operation(a,b);
        }
    }

    public Vector3 operation (Vector3 a, Vector3 b) throws SpellRuntimeException{
        return new Vector3(operation(a.x,b.x),operation(a.y,b.y),operation(a.z,b.z));
    }

    public abstract double operation (double a, double b) throws SpellRuntimeException;
}
