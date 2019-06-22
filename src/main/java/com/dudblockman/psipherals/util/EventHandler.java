package com.dudblockman.psipherals.util;

import com.dudblockman.psipherals.Psipherals;
import com.dudblockman.psipherals.entity.EntityPsiArrow;
import com.dudblockman.psipherals.entity.capability.ArrowSpellImmuneCapability;
import com.teamwizardry.librarianlib.features.helpers.NBTHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.psi.common.entity.EntitySpellProjectile;


@Mod.EventBusSubscriber(modid = Psipherals.MODID)
public class EventHandler {

    public static final String TAG_SPELLIMMUNE = "rpsideas-spellimmune";

    @SubscribeEvent
    public static void attachArrowSpellImmunity(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityArrow) {
            event.addCapability(new ResourceLocation(Psipherals.MODID,"psipheralsspellimmunearrow"), new ArrowSpellImmuneCapability(event.getObject()));
        }

    }

    @SubscribeEvent
    public static void arrowHit(ProjectileImpactEvent event) {
        Entity projectile = event.getEntity();
        if ((projectile instanceof EntityArrow) && !(projectile instanceof EntityPsiArrow)) {
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
        }
    }
}
