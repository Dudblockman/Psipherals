package com.dudblockman.psipherals.spell.operator;

import net.minecraft.entity.Entity;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.wrapper.EntityListWrapper;

import java.util.ArrayList;
import java.util.List;

public class OperatorParallelMaximum extends OperatorParallel {
    public OperatorParallelMaximum(Spell spell) {
        super(spell);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        EntityListWrapper listVal = this.getParamValueOrDefault(context, entList, EntityListWrapper.EMPTY);
        if (this.processExecution(listVal, context)) {
            int maxIndex = 0;
            for (int i = 0; i < listVal.size(); i++) {
                maxIndex = inputs.get(i) > inputs.get(maxIndex) ? i : maxIndex;
            }
            return listVal.size() == 0 ? null : listVal.get(maxIndex);
        }
        return null;
    }

    @Override
    public Class<?> getEvaluationType() {
        return Entity.class;
    }
}
