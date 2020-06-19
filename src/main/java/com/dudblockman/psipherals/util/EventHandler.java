package com.dudblockman.psipherals.util;

import com.dudblockman.psipherals.Psipherals;
import com.dudblockman.psipherals.entity.capability.ArrowSpellImmuneCapability;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

//import com.dudblockman.psipherals.entity.EntityPsiArrow;
//import com.teamwizardry.librarianlib.features.helpers.NBTHelper;


@Mod.EventBusSubscriber(modid = Psipherals.MODID)
public class EventHandler {

    public static final String TAG_SPELLIMMUNE = "psipherals-spellimmune";

    @SubscribeEvent
    public static void attachArrowSpellImmunity(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof AbstractArrowEntity) {
            event.addCapability(new ResourceLocation(Psipherals.MODID,"psipheralsspellimmunearrow"), new ArrowSpellImmuneCapability(event.getObject()));
        }

    }

    @SubscribeEvent
    public static void arrowHit(ProjectileImpactEvent event) {
        Entity projectile = event.getEntity();
        /*if ((projectile instanceof AbstractArrowEntity) ) {
            if (NBTHelper.hasKey(projectile.getEntityData(), TAG_SPELLIMMUNE)) {
                for (Entity rider : projectile.getPassengers()) {
                    if (rider instanceof EntitySpellProjectile) {
                        rider.dismountRidingEntity();
                        rider.setPosition(projectile.posX, projectile.posY, projectile.posZ);
                        rider.motionX = projectile.motionX;
                        rider.motionY = projectile.motionY;
                        rider.motionZ = projectile.motionZ;
                        rider.velocityChanged = true;
                    }
                }
            }
        }*/
    }
}
