package com.dudblockman.psipherals.spell.operator;

import net.minecraft.entity.Entity;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.wrapper.EntityListWrapper;

import java.util.ArrayList;
import java.util.List;

public class OperatorParallelFilter extends OperatorParallel {
    public OperatorParallelFilter(Spell spell) {
        super(spell);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        EntityListWrapper listVal = this.getParamValueOrDefault(context, entList, EntityListWrapper.EMPTY);
        if (this.processExecution(listVal, context)) {
            List<Entity> newList = new ArrayList<>();
            for (int i = 0; i < listVal.size(); i++) {
                if (Math.abs(this.inputs.get(i)) >= 1) {
                    newList.add(listVal.get(i));
                }
            }
            return EntityListWrapper.make(newList);
        }
        return null;
    }

    @Override
    public Class<?> getEvaluationType() {
        return EntityListWrapper.class;
    }
}
