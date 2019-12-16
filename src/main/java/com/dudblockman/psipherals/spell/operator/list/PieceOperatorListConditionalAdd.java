package com.dudblockman.psipherals.spell.operator.list;

import net.minecraft.entity.Entity;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamEntity;
import vazkii.psi.api.spell.param.ParamEntityListWrapper;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceOperator;
import vazkii.psi.api.spell.wrapper.EntityListWrapper;

import java.util.ArrayList;
import java.util.List;

public class PieceOperatorListConditionalAdd extends PieceOperator {

    SpellParam target;
    SpellParam list;
    SpellParam condition;

    public PieceOperatorListConditionalAdd(Spell spell) {
        super(spell);
    }


    @Override
    public void initParams() {
        addParam(target = new ParamEntity(SpellParam.GENERIC_NAME_TARGET, SpellParam.BLUE, false, false));
        addParam(list = new ParamEntityListWrapper("psi.spellparam.list", SpellParam.RED, false, false));
        addParam(condition = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER, SpellParam.YELLOW, false, false));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Entity targetVal = this.getParamValue(context, target);
        EntityListWrapper listVal = this.getParamValue(context, list);
        Double conditionVal = this.getParamValue(context, condition);
        if(targetVal == null)
            throw new SpellRuntimeException(SpellRuntimeException.NULL_TARGET);
        if(!(Math.abs(conditionVal) < 1)) {
            List<Entity> list = new ArrayList<>(listVal.unwrap());

            if (!list.contains(targetVal))
                list.add(targetVal);

            return new EntityListWrapper(list);
        } else {
            return listVal;
        }
    }

    @Override
    public Class<?> getEvaluationType() {
        return EntityListWrapper.class;
    }
}
