package com.dudblockman.psipherals.util.network;


import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData;

import java.util.function.Supplier;

public class MessageShatter {

    public MessageShatter() {
    }

    public MessageShatter(PacketBuffer packetBuffer) {
    }

    public void encode(PacketBuffer buf) {
    }

    public boolean receive(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            PlayerEntity player = context.get().getSender();
            if (player == null) {
                player = Minecraft.getInstance().player;
            }
            PlayerData data = PlayerDataHandler.get(player);
            data.overflowed = true;
        });

        return true;
    }
}
