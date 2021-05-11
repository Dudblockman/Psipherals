package com.dudblockman.psipherals.spell.other;

import vazkii.psi.api.spell.*;

public class PieceCADOverclock extends SpellPiece {
    public static String OVERCLOCK = "psipherals.overclocked";
    public static double DISCOUNT_MULTIPLIER = 0.75;
    public static double BURNOUT_MULTIPLIER = 1.0;
    public static double POTENCY_MULTIPLIER = 0.7;

    public PieceCADOverclock(Spell spell) {
        super(spell);
    }
    @Override
    public void addToMetadata(SpellMetadata meta) {
        if (!meta.getFlag(OVERCLOCK)) {
            meta.compoundStatMultiplier(EnumSpellStat.COST, DISCOUNT_MULTIPLIER);
            meta.compoundStatMultiplier(EnumSpellStat.POTENCY, POTENCY_MULTIPLIER);
        }
        meta.setFlag(OVERCLOCK, true);

    }

    @Override
    public EnumPieceType getPieceType() {
        return EnumPieceType.MODIFIER;
    }

    @Override
    public Class<?> getEvaluationType() {
        return Void.class;
    }

    @Override
    public Object evaluate() throws SpellCompilationException {
        return null;
    }

    @Override
    public Object execute(SpellContext spellContext) throws SpellRuntimeException {
        return null;
    }
}
