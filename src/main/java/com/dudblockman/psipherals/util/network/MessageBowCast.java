package com.dudblockman.psipherals.util.network;

import com.dudblockman.psipherals.items.ItemBowCad;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Hand;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageBowCast {

    public MessageBowCast() {
    }

    public MessageBowCast(PacketBuffer packetBuffer) {
    }

    public void encode(PacketBuffer buf) {
    }

    public boolean receive(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayerEntity player = context.get().getSender();
            ItemStack stack = player.getHeldItem(Hand.MAIN_HAND);
            if (!stack.isEmpty() && stack.getItem() instanceof ItemBowCad) {
                ((ItemBowCad) stack.getItem()).castSpell(player, stack);
            }
        });

        return true;
    }
}
