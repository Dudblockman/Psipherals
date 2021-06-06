package com.dudblockman.psipherals.spell.operator.overrides;

import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;

public abstract class OperatorAdaptiveSingleBase extends OperatorAdaptiveBase {

    SpellParam<VectorOrNumber> in1;

    public OperatorAdaptiveSingleBase(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(in1 = new ParamVectorNumber(SpellParam.GENERIC_NAME_TARGET, SpellParam.BLUE, false));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Object d1 = this.getParamValue(context, in1);
        if (d1 instanceof Vector3 ) {
            return operation(vectorize(d1));
        } else {
            return operation(((Number) d1).doubleValue());
        }
    }

    public Vector3 operation (Vector3 a) throws SpellRuntimeException {
        return new Vector3(operation(a.x),operation(a.y),operation(a.z));
    }

    public abstract double operation (double a) throws SpellRuntimeException;
}
