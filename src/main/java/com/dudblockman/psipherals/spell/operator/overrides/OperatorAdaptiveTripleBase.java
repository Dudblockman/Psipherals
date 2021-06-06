package com.dudblockman.psipherals.spell.operator.overrides;

import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;

public abstract class OperatorAdaptiveTripleBase extends OperatorAdaptiveBase {

    SpellParam<VectorOrNumber> in1;
    SpellParam<VectorOrNumber> in2;
    SpellParam<VectorOrNumber> in3;

    public OperatorAdaptiveTripleBase(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(in1 = new ParamVectorNumber(SpellParam.GENERIC_NAME_NUMBER1, SpellParam.GREEN, false));
        addParam(in2 = new ParamVectorNumber(SpellParam.GENERIC_NAME_NUMBER2, SpellParam.GREEN, false));
        addParam(in3 = new ParamVectorNumber(SpellParam.GENERIC_NAME_NUMBER3, SpellParam.GREEN, true));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Object d1 = this.getParamValue(context, in1);
        Object d2 = this.getParamValue(context, in2);
        Object d3 = this.getParamValue(context, in3);
        if (d1 instanceof Vector3 || d2 instanceof Vector3 || d3 instanceof Vector3) {
            return operation(vectorize(d1), vectorize(d2), vectorize(d3));
        } else {
            double a = ((Number) d1).doubleValue();
            double b = ((Number) d2).doubleValue();
            double c = defaultValue;
            if (d3 != null) {
                c = ((Number) d3).doubleValue();
            }
            return operation(a,b,c);
        }
    }

    public Vector3 operation (Vector3 a, Vector3 b, Vector3 c) throws SpellRuntimeException{
        return new Vector3(operation(a.x,b.x,c.x),operation(a.y,b.y,c.y),operation(a.z,b.z,c.z));
    }

    public abstract double operation (double a, double b, double c) throws SpellRuntimeException;
}
