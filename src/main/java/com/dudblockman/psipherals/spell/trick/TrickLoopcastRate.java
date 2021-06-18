package com.dudblockman.psipherals.spell.trick;

import com.dudblockman.psipherals.util.PlayerDataWrapper;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.core.handler.PlayerDataHandler;

public class TrickLoopcastRate extends PieceTrick {
    SpellParam<Number> time;

    public TrickLoopcastRate(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        this.addParam(this.time = new ParamNumber("psi.spellparam.time", SpellParam.BLUE, false, true));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);

        Double timeVal = this.getParamEvaluation(this.time);
        if (timeVal != null && timeVal == (double) timeVal.intValue()) {
            meta.addStat(EnumSpellStat.POTENCY, (int) (20 + timeVal));
        } else {
            throw new SpellCompilationException("psi.spellerror.nonpositiveinteger", this.x, this.y);
        }
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        if (context.loopcastIndex != 0)
            return null;
        int timeVal = (int) ((Number) this.getParamValue(context, this.time)).doubleValue();
        timeVal = Math.max(1, timeVal);
        PlayerDataHandler.PlayerData data = PlayerDataHandler.get(context.caster);
        ((PlayerDataWrapper) data).setLoopcastRate(timeVal);
        return null;
    }

}
