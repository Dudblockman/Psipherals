package com.dudblockman.psipherals.spell.selector;

import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.piece.PieceSelector;

public class SelectorAltFire extends PieceSelector {
    public static String ALTFIREKEY = "psipherals.altfire";

    public SelectorAltFire(Spell spell) {
        super(spell);
    }

    @Override
    public Class<?> getEvaluationType() {
        return Double.class;
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        return context.customData.getOrDefault(ALTFIREKEY, 0);
    }
}
