package com.dudblockman.psipherals.spell.base;

import com.dudblockman.psipherals.Psipherals;
import com.dudblockman.psipherals.spell.operator.list.PieceOperatorListConditionalAdd;
import com.dudblockman.psipherals.spell.operator.list.PieceOperatorListConditionalExclusion;
import com.dudblockman.psipherals.spell.operator.list.PieceOperatorListConditionalRemove;
import com.dudblockman.psipherals.spell.operator.list.PieceOperatorListConditionalUnion;
import com.dudblockman.psipherals.spell.selector.PieceSelectorSavedBinary;
import com.dudblockman.psipherals.spell.trick.PieceTrickSaveBinary;
import com.dudblockman.psipherals.spell.trick.entity.PieceTrickRedirectMotion;
import com.dudblockman.psipherals.util.libs.PieceNames;
import net.minecraft.util.ResourceLocation;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.common.lib.LibPieceGroups;

public class SpellPieces {
    public static void init() {
        //register(PieceTrickRedirectMotion.class, PieceNames.REDIRECT_MOTION, LibPieceGroups.MOVEMENT);

        register(PieceTrickSaveBinary.class, PieceNames.SAVE_BINARY, LibPieceGroups.MEMORY_MANAGEMENT);
        register(PieceSelectorSavedBinary.class, PieceNames.LOAD_BINARY, LibPieceGroups.MEMORY_MANAGEMENT);

        register(PieceOperatorListConditionalAdd.class, PieceNames.LIST_CON_ADD, LibPieceGroups.ENTITIES_INTRO);
        register(PieceOperatorListConditionalRemove.class, PieceNames.LIST_CON_REMOVE, LibPieceGroups.ENTITIES_INTRO);

        register(PieceOperatorListConditionalUnion.class, PieceNames.LIST_CON_UNION, LibPieceGroups.ENTITIES_INTRO);
        register(PieceOperatorListConditionalExclusion.class, PieceNames.LIST_CON_EXCLUSION, LibPieceGroups.ENTITIES_INTRO);
        register(PieceOperatorListConditionalAdd.class, PieceNames.LIST_CON_ADD, LibPieceGroups.ENTITIES_INTRO);
    }
    public static void register(Class<? extends SpellPiece> pieceClass, String name, String group) {
        register(pieceClass, name, group, false);
    }

    public static void register(Class<? extends SpellPiece> pieceClass, String name, String group, boolean main) {
        String key = Psipherals.MODID + "." + name;
        PsiAPI.registerSpellPiece(key, pieceClass);
        registerTexture(name, Psipherals.MODID, name);
        PsiAPI.addPieceToGroup(pieceClass, group, main);
    }

    public static void registerNoTexture(Class<? extends SpellPiece> pieceClass, String name, String group) {
        registerNoTexture(pieceClass, name, group, false);
    }

    public static void registerNoTexture(Class<? extends SpellPiece> pieceClass, String name, String group, boolean main) {
        PsiAPI.registerSpellPiece(Psipherals.MODID + "." + name, pieceClass);
        PsiAPI.addPieceToGroup(pieceClass, group, main);
    }

    public static void registerTexture(String name, String modId, String texture) {
        PsiAPI.simpleSpellTextures.put(Psipherals.MODID + "." + name, new ResourceLocation(modId, "textures/spell/" + texture + ".png"));

    }
}
