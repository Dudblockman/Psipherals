package com.dudblockman.psipherals.spell.trick.internal;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellMetadata;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.item.ItemCAD;

public class TrickExecuteAmulet extends PieceTrick {

    public TrickExecuteAmulet(Spell spell) {
        super(spell);
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException, ArithmeticException {
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        ItemStack tool = context.tool;
        if (!tool.isEmpty()) {
            PlayerEntity player = context.caster;
            PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
            ItemStack playerCad = PsiAPI.getPlayerCAD(player);
            ItemStack bullet = ISocketable.socketable(tool).getSelectedBullet();
            if (bullet == ItemStack.EMPTY) return null;
            ItemCAD.cast(player.getEntityWorld(), player, data, bullet, playerCad, 40, 0, 1, (SpellContext context2) -> {
                context2.tool = tool;
            });
        }
        return null;
    }
}
