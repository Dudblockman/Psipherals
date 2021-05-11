package com.dudblockman.psipherals.util;

import com.dudblockman.psipherals.Psipherals;
import com.dudblockman.psipherals.spell.other.PieceCADOverclock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vazkii.psi.api.spell.PreSpellCastEvent;

@Mod.EventBusSubscriber(modid= Psipherals.MODID)
public class BurnoutEventSubscriber {
    @SubscribeEvent
    public static void applyBurnout(PreSpellCastEvent event) {
        if (event.getCost() > 0 && event.getContext().cspell.metadata.getFlag(PieceCADOverclock.OVERCLOCK)) {
            ((PlayerDataWrapper) event.getPlayerData()).addBurnout((int) (event.getCost() * PieceCADOverclock.BURNOUT_MULTIPLIER));
        }
    }
}
