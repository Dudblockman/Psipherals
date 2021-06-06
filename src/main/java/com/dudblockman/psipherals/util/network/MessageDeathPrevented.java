package com.dudblockman.psipherals.util.network;


import com.dudblockman.psipherals.items.Items;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageDeathPrevented {

    public MessageDeathPrevented() {
    }

    public MessageDeathPrevented(PacketBuffer packetBuffer) {
    }

    public void encode(PacketBuffer buf) {
    }
    public boolean receive(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            boolean did = false;
            do {
                if (Math.random() * 1000 < 1) {
                    ItemStack stack = new ItemStack(Items.psiAmulet);
                    CompoundNBT tag = stack.getOrCreateTag();
                    tag.putBoolean("sus", true);
                    stack.setTag(tag);
                    Minecraft.getInstance().gameRenderer.displayItemActivation(stack);
                    assert Minecraft.getInstance().player != null;
                    Minecraft.getInstance().player.playSound(SoundEvents.ITEM_TOTEM_USE,1,1);
                    did = true;
                }
            } while (!did);
        });

        return true;
    }
}
