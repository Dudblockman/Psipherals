package com.dudblockman.psipherals.spell.base;

import com.dudblockman.psipherals.Psipherals;
import com.dudblockman.psipherals.spell.operator.OperatorDistanceFromGround;
import com.dudblockman.psipherals.spell.operator.OperatorParallelFilter;
import com.dudblockman.psipherals.spell.operator.OperatorParallelMaximum;
import com.dudblockman.psipherals.spell.operator.overrides.*;
import com.dudblockman.psipherals.spell.other.PieceCADOverclock;
import com.dudblockman.psipherals.spell.selector.SelectorAltFire;
import com.dudblockman.psipherals.spell.selector.SelectorParallelEntity;
import com.dudblockman.psipherals.spell.selector.SelectorParallelIndex;
import com.dudblockman.psipherals.spell.trick.TrickBulwark;
import com.dudblockman.psipherals.spell.trick.TrickFork;
import com.dudblockman.psipherals.spell.trick.TrickPsilon;
import com.dudblockman.psipherals.util.libs.PieceNames;
import net.minecraft.util.ResourceLocation;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.common.lib.LibPieceGroups;
import vazkii.psi.common.lib.LibPieceNames;
import vazkii.psi.common.spell.base.ModSpellPieces;

public class SpellPieces {
    public static void init() {
        //register(TrickKingCrimson.class, PieceNames.KING_CRIMSON, LibPieceGroups.MOVEMENT);
        register(TrickFork.class, PieceNames.FORK, LibPieceGroups.PROJECTILES);
        register(TrickBulwark.class, PieceNames.BULWARK, LibPieceGroups.POSITIVE_EFFECTS);
        register(SelectorAltFire.class, PieceNames.ALT_FIRE, LibPieceGroups.TOOL_CASTING);

        register(OperatorParallelFilter.class, PieceNames.PARALLEL_FILTER, LibPieceGroups.ENTITIES_INTRO);
        register(OperatorParallelMaximum.class, PieceNames.PARALLEL_MAXIMUM, LibPieceGroups.ENTITIES_INTRO);
        register(SelectorParallelIndex.class, PieceNames.PARALLEL_INDEX, LibPieceGroups.ENTITIES_INTRO);
        register(SelectorParallelEntity.class, PieceNames.PARALLEL_ENTITY, LibPieceGroups.ENTITIES_INTRO);

        register(OperatorDistanceFromGround.class, PieceNames.DISTANCE_FROM_GROUND, LibPieceGroups.SECONDARY_OPERATORS);

        register(PieceCADOverclock.class, PieceNames.OVERCLOCK, LibPieceGroups.EIDOS_REVERSAL);

        register(TrickPsilon.class, PieceNames.PSILON, LibPieceGroups.GREATER_INFUSION);

        //Vanilla Psi Registry Replacements
        ModSpellPieces.register(OperatorAdaptiveSum.class, LibPieceNames.OPERATOR_SUM, LibPieceGroups.NUMBERS_INTRO, true);
        ModSpellPieces.register(OperatorAdaptiveSubtract.class, LibPieceNames.OPERATOR_SUBTRACT, LibPieceGroups.NUMBERS_INTRO);
        ModSpellPieces.register(OperatorAdaptiveMultiply.class, LibPieceNames.OPERATOR_MULTIPLY, LibPieceGroups.NUMBERS_INTRO);
        ModSpellPieces.register(OperatorAdaptiveDivide.class, LibPieceNames.OPERATOR_DIVIDE, LibPieceGroups.NUMBERS_INTRO);
        ModSpellPieces.register(OperatorAdaptiveAbsolute.class, LibPieceNames.OPERATOR_ABSOLUTE, LibPieceGroups.NUMBERS_INTRO);
        ModSpellPieces.register(OperatorAdaptiveInverse.class, LibPieceNames.OPERATOR_INVERSE, LibPieceGroups.NUMBERS_INTRO);

        ModSpellPieces.register(OperatorAdaptiveModulus.class, LibPieceNames.OPERATOR_MODULUS, LibPieceGroups.LOOPCASTING);
        ModSpellPieces.register(OperatorAdaptiveIntegerDivide.class, LibPieceNames.OPERATOR_INTEGER_DIVIDE, LibPieceGroups.LOOPCASTING);

        ModSpellPieces.register(OperatorAdaptiveRandom.class, LibPieceNames.OPERATOR_RANDOM, LibPieceGroups.ELEMENTAL_ARTS);

        ModSpellPieces.register(OperatorAdaptiveSin.class, LibPieceNames.OPERATOR_SIN, LibPieceGroups.TRIGONOMETRY);
        ModSpellPieces.register(OperatorAdaptiveCos.class, LibPieceNames.OPERATOR_COS, LibPieceGroups.TRIGONOMETRY);
        ModSpellPieces.register(OperatorAdaptiveAsin.class, LibPieceNames.OPERATOR_ASIN, LibPieceGroups.TRIGONOMETRY);
        ModSpellPieces.register(OperatorAdaptiveAcos.class, LibPieceNames.OPERATOR_ACOS, LibPieceGroups.TRIGONOMETRY);

        ModSpellPieces.register(OperatorAdaptiveMin.class, LibPieceNames.OPERATOR_MIN, LibPieceGroups.SECONDARY_OPERATORS);
        ModSpellPieces.register(OperatorAdaptiveMax.class, LibPieceNames.OPERATOR_MAX, LibPieceGroups.SECONDARY_OPERATORS);
        ModSpellPieces.register(OperatorAdaptiveSquare.class, LibPieceNames.OPERATOR_SQUARE, LibPieceGroups.SECONDARY_OPERATORS, true);
        ModSpellPieces.register(OperatorAdaptiveCube.class, LibPieceNames.OPERATOR_CUBE, LibPieceGroups.SECONDARY_OPERATORS);
        ModSpellPieces.register(OperatorAdaptivePower.class, LibPieceNames.OPERATOR_POWER, LibPieceGroups.SECONDARY_OPERATORS);
        ModSpellPieces.register(OperatorAdaptiveSquareRoot.class, LibPieceNames.OPERATOR_SQUARE_ROOT, LibPieceGroups.SECONDARY_OPERATORS);
        ModSpellPieces.register(OperatorAdaptiveLog.class, LibPieceNames.OPERATOR_LOG, LibPieceGroups.SECONDARY_OPERATORS);
        ModSpellPieces.register(OperatorAdaptiveCeiling.class, LibPieceNames.OPERATOR_CEILING, LibPieceGroups.SECONDARY_OPERATORS);
        ModSpellPieces.register(OperatorAdaptiveFloor.class, LibPieceNames.OPERATOR_FLOOR, LibPieceGroups.SECONDARY_OPERATORS);
        ModSpellPieces.register(OperatorAdaptiveRound.class, LibPieceNames.OPERATOR_ROUND, LibPieceGroups.SECONDARY_OPERATORS);

        ModSpellPieces.register(OperatorAdaptiveRoot.class, LibPieceNames.OPERATOR_ROOT, LibPieceGroups.NUMBERS_INTRO);

        ModSpellPieces.register(OperatorAdaptiveSignum.class, LibPieceNames.OPERATOR_SIGNUM, LibPieceGroups.TRIGONOMETRY);
    }

    public static ModSpellPieces.PieceContainer register(Class<? extends SpellPiece> clazz, String name, String group) {
        return register(clazz, name, group, false);
    }

    public static ModSpellPieces.PieceContainer register(Class<? extends SpellPiece> clazz, String name, String group, boolean main) {
        PsiAPI.registerSpellPieceAndTexture(Psipherals.location(name), clazz);
        PsiAPI.addPieceToGroup(clazz, new ResourceLocation("psi", group), main);
        return (Spell s) -> SpellPiece.create(clazz, s);
    }
}
