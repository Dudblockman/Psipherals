package com.dudblockman.psipherals.util;

import com.dudblockman.psipherals.Psipherals;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import vazkii.psi.common.entity.EntitySpellProjectile;

//import com.dudblockman.psipherals.entity.EntityPsiArrow;
//import com.teamwizardry.librarianlib.features.helpers.NBTHelper;


@Mod.EventBusSubscriber(modid = Psipherals.MODID)
public class EventHandler {

    public static final String TAG_SPELLIMMUNE = "psipherals-spellimmune";

    @SubscribeEvent
    public static void arrowHit(ProjectileImpactEvent event) {
        Entity projectile = event.getEntity();
        ejectRidingSpells(projectile);
    }

    public static void ejectRidingSpells(Entity projectile) {
        if ((projectile instanceof AbstractArrowEntity) || (projectile instanceof FireworkRocketEntity)) {
            for (Entity rider : projectile.getPassengers()) {
                if (rider instanceof EntitySpellProjectile) {
                    rider.stopRiding();
                    rider.setPosition(projectile.getPosX(), projectile.getPosY(), projectile.getPosZ());
                    rider.setMotion(projectile.getMotion());
                    rider.velocityChanged = true;
                }
            }
        }
    }
}
