package com.dudblockman.psipherals.spell.base;

import com.dudblockman.psipherals.Psipherals;
import com.dudblockman.psipherals.spell.operator.OperatorParallelFilter;
import com.dudblockman.psipherals.spell.operator.OperatorParallelMaximum;
import com.dudblockman.psipherals.spell.selector.SelectorAltFire;
import com.dudblockman.psipherals.spell.selector.SelectorParallelEntity;
import com.dudblockman.psipherals.spell.selector.SelectorParallelIndex;
import com.dudblockman.psipherals.util.libs.PieceNames;
import net.minecraft.util.ResourceLocation;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.common.lib.LibPieceGroups;
import vazkii.psi.common.spell.base.ModSpellPieces;

public class SpellPieces {
    public static void init() {
        //register(TrickKingCrimson.class, PieceNames.KING_CRIMSON, LibPieceGroups.MOVEMENT);
        register(SelectorAltFire.class, PieceNames.ALT_FIRE, LibPieceGroups.TOOL_CASTING);
        register(OperatorParallelFilter.class, PieceNames.PARALLEL_FILTER, LibPieceGroups.ENTITIES_INTRO);
        register(OperatorParallelMaximum.class, PieceNames.PARALLEL_MAXIMUM, LibPieceGroups.ENTITIES_INTRO);
        register(SelectorParallelIndex.class, PieceNames.PARALLEL_INDEX, LibPieceGroups.ENTITIES_INTRO);
        register(SelectorParallelEntity.class, PieceNames.PARALLEL_ENTITY, LibPieceGroups.ENTITIES_INTRO);
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
