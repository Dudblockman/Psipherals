package com.dudblockman.psipherals.spell.selector;

import com.dudblockman.psipherals.spell.operator.OperatorParallel;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellRuntimeException;


public class SelectorParallelIndex extends SelectorParallel {
    public SelectorParallelIndex(Spell spell) {
        super(spell);
    }

    @Override
    public Class<?> getEvaluationType() {
        return Double.class;
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        if (context.customData.containsKey(OperatorParallel.INDEX)) {
            return context.customData.get(OperatorParallel.INDEX);
        }
        this.TriggerParallel(context);
        return null;
    }
}
