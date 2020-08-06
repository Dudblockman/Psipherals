package com.dudblockman.psipherals.entity;

import com.dudblockman.psipherals.Psipherals;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = Psipherals.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Entities {

    public static final String PSI_ARROW = "psi_arrow";
    public static EntityType<EntityPsiArrow> arrowEntityType;

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> evt) {
        IForgeRegistry<EntityType<?>> r = evt.getRegistry();
        arrowEntityType = EntityType.Builder.<EntityPsiArrow>create(EntityPsiArrow::new, EntityClassification.MISC)
                .size(0.5F, 0.5F)
                //.setTrackingRange(80)
                //.setUpdateInterval(3)
                //.setShouldReceiveVelocityUpdates(true)
                .setCustomClientFactory((spawnEntity, world) -> new EntityPsiArrow(arrowEntityType, world))
                .build(PSI_ARROW);
        r.register(arrowEntityType.setRegistryName(Psipherals.location(PSI_ARROW)));

    }
}
