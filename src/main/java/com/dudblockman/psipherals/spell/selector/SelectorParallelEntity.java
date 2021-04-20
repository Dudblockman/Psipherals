package com.dudblockman.psipherals.spell.selector;

import com.dudblockman.psipherals.spell.operator.OperatorParallel;
import net.minecraft.entity.Entity;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellRuntimeException;

public class SelectorParallelEntity extends SelectorParallel {
    public SelectorParallelEntity(Spell spell) {
        super(spell);
    }

    @Override
    public Class<?> getEvaluationType() {
        return Entity.class;
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        if (context.customData.containsKey(OperatorParallel.ENTITY)) {
            return context.customData.get(OperatorParallel.ENTITY);
        } else if (!this.TriggerParallel(context)) {
            throw new SpellRuntimeException("Not Parallel");
        }
        return null;
    }
}
