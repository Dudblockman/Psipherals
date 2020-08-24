package com.dudblockman.psipherals.util.network;

import com.dudblockman.psipherals.Psipherals;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class MessageRegistry {
    private static final String VERSION = "1";
    public static final SimpleChannel HANDLER = NetworkRegistry.newSimpleChannel(Psipherals.location("main"),
            () -> VERSION,
            VERSION::equals,
            VERSION::equals);
    public static void init() {
        int id = 0;
        HANDLER.messageBuilder(MessageBowCast.class, id++)
                .encoder(MessageBowCast::encode)
                .decoder(MessageBowCast::new)
                .consumer(MessageBowCast::receive).add();
    }
    public static void sendToPlayer(Object msg, PlayerEntity player) {
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
        HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), msg);
    }
    public static void sendToServer(Object msg) {
        HANDLER.sendToServer(msg);
    }
}
