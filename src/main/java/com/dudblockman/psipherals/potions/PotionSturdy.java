package com.dudblockman.psipherals.potions;

import com.dudblockman.psipherals.Psipherals;
import com.dudblockman.psipherals.util.network.MessageRegistry;
import com.dudblockman.psipherals.util.network.MessageShatter;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler.PlayerData;

@Mod.EventBusSubscriber(modid= Psipherals.MODID)
public class PotionSturdy extends Effect {
    protected PotionSturdy(EffectType typeIn, int liquidColorIn) {
        super(typeIn, liquidColorIn);
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (!event.getSource().canHarmInCreative()) {
            LivingEntity victim = event.getEntityLiving();
            EffectInstance effect = victim.getActivePotionEffect(Potions.sturdy);
            if(effect != null) {
                int amplifier = effect.getAmplifier();
                int duration = effect.getDuration();
                victim.removePotionEffect(Potions.sturdy);
                if (amplifier > 0) {
                    victim.addPotionEffect(new EffectInstance(Potions.sturdy, duration, amplifier-1));
                }
                victim.setHealth(1);
                victim.playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.8F, 0.8F + victim.world.rand.nextFloat() * 0.4F);
                if (victim instanceof PlayerEntity) {
                    PlayerData data = PlayerDataHandler.get((PlayerEntity)victim);
                    data.overflowed = true;
                    MessageRegistry.sendToPlayer(new MessageShatter(), (PlayerEntity)victim);
                }
                event.setCanceled(true);
            }
        }
    }
}
