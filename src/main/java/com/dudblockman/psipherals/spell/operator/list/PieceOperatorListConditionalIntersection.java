package com.dudblockman.psipherals.spell.operator.list;

import com.dudblockman.psipherals.spell.base.PsipheralsSpellParam;
import net.minecraft.entity.Entity;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamEntityListWrapper;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceOperator;
import vazkii.psi.api.spell.wrapper.EntityListWrapper;

import java.util.ArrayList;
import java.util.List;

public class PieceOperatorListConditionalIntersection extends PieceOperator {

    SpellParam list1;
    SpellParam list2;
    SpellParam condition;

    public PieceOperatorListConditionalIntersection(Spell spell) {
        super(spell);
    }


    @Override
    public void initParams() {
        addParam(list1 = new ParamEntityListWrapper(PsipheralsSpellParam.GENERIC_NAME_LIST1, SpellParam.BLUE, false, false));
        addParam(list2 = new ParamEntityListWrapper(PsipheralsSpellParam.GENERIC_NAME_LIST2, SpellParam.RED, false, false));
        addParam(condition = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER, SpellParam.YELLOW, false, false));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        EntityListWrapper list1Val = this.getParamValue(context, list1);
        EntityListWrapper list2Val = this.getParamValue(context, list2);
        Double conditionVal = this.getParamValue(context, condition);
        if(!(Math.abs(conditionVal) < 1)) {
            List<Entity> list1 = new ArrayList<>(list1Val.unwrap());
            List<Entity> list2 = new ArrayList<>(list2Val.unwrap());
            
            list1.retainAll(list2);

            return new EntityListWrapper(list1);
        } else {
            return list1Val;
        }
    }

    @Override
    public Class<?> getEvaluationType() {
        return EntityListWrapper.class;
    }
}
