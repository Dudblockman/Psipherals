package com.dudblockman.psipherals.spell.base;

import com.dudblockman.psipherals.Psipherals;
import com.dudblockman.psipherals.spell.trick.entity.TrickKingCrimson;
import com.dudblockman.psipherals.util.libs.PieceNames;
import net.minecraft.util.ResourceLocation;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.common.lib.LibPieceGroups;
import vazkii.psi.common.spell.base.ModSpellPieces;

public class SpellPieces {
    public static void init() {
        //register(TrickExecuteAmulet.class, PieceNames.TRIGGER_AMULET, "null");
        //register(TrickKingCrimson.class, PieceNames.KING_CRIMSON, LibPieceGroups.MOVEMENT);
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
