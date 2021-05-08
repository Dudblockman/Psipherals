package com.dudblockman.psipherals.potions;

import com.dudblockman.psipherals.Psipherals;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Psipherals.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Potions {
    public static Effect sturdy = new PotionSturdy(EffectType.BENEFICIAL,745784);
    @SubscribeEvent
    public static void registerEffect(RegistryEvent.Register<Effect> evt) {
        evt.getRegistry().register(sturdy.setRegistryName(Psipherals.location("sturdy")));
    }
}
