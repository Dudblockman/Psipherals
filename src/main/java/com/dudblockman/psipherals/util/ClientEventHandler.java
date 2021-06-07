package com.dudblockman.psipherals.util;

import com.dudblockman.psipherals.items.ItemBowCad;
import com.dudblockman.psipherals.util.network.MessageBowCast;
import com.dudblockman.psipherals.util.network.MessageRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ClientEventHandler {
    @SubscribeEvent
    public static void bowLeftClickAir(PlayerInteractEvent.LeftClickEmpty event) {
        ItemStack heldItem = event.getPlayer().getHeldItemMainhand();
        if (heldItem.getItem() instanceof ItemBowCad) {
            MessageRegistry.sendToServer(new MessageBowCast());
        }
    }

    @SubscribeEvent
    public static void bowLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        ItemStack heldItem = event.getPlayer().getHeldItemMainhand();
        if (heldItem.getItem() instanceof ItemBowCad) {
            event.setCancellationResult(ActionResultType.FAIL);
            event.setCanceled(true);
            MessageRegistry.sendToServer(new MessageBowCast());
        }
    }
}
