package com.dudblockman.psipherals.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.spell.ISpellImmune;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.detonator.IDetonationHandler;
import vazkii.psi.common.entity.EntitySpellCharge;
import vazkii.psi.common.entity.EntitySpellGrenade;
import vazkii.psi.common.entity.EntitySpellMine;
import vazkii.psi.common.entity.EntitySpellProjectile;
import vazkii.psi.common.item.ItemSpellBullet;
import vazkii.psi.common.item.ItemSpellDrive;

public class EntityPsiArrow extends EntityTippedArrow implements ISpellImmune, IDetonationHandler {
    ItemStack spellBullet;
    SpellContext context;
    public EntityPsiArrow(World worldIn) {
        super(worldIn);
    }
    public EntityPsiArrow(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
    }
    public EntityPsiArrow(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    protected void castSpell(ItemStack stack, SpellContext context) {
        ItemStack cad = PsiAPI.getPlayerCAD(context.caster);
        ItemStack colorizer = ((ICAD) cad.getItem()).getComponentInSlot(cad, EnumCADComponent.DYE);

        EntitySpellProjectile projectile = null;

        switch (stack.getItemDamage()) {
            case 1: // Basic
                //context.cspell.safeExecute(context);
                break;

            case 3: // Projectile
                projectile = new EntitySpellProjectile(context.caster.getEntityWorld(), context.caster);
                break;

            case 5: // Loopcast
                /*PlayerDataHandler.PlayerData data = PlayerDataHandler.get(context.caster);
                if (!data.loopcasting || context.castFrom != data.loopcastHand) {
                    context.cspell.safeExecute(context);
                    data.loopcasting = true;
                    data.loopcastHand = context.castFrom;
                    data.lastTickLoopcastStack = null;
                    if (context.caster instanceof EntityPlayerMP)
                        LoopcastTrackingHandler.syncForTrackers((EntityPlayerMP) context.caster);
                }*/
                break;

            case 7: // Spell Circle
                /*RayTraceResult pos = PieceOperatorVectorRaycast.raycast(context.caster, 32);

                if (pos != null) {
                    EntitySpellCircle circle = new EntitySpellCircle(context.caster.getEntityWorld());
                    circle.setInfo(context.caster, colorizer, stack);
                    circle.setPosition(pos.hitVec.x, pos.hitVec.y, pos.hitVec.z);
                    circle.getEntityWorld().spawnEntity(circle);
                }*/
                break;

            case 9: // Grenade
                projectile = new EntitySpellGrenade(context.caster.getEntityWorld(), context.caster);
                break;

            case 11: // Charge
                projectile = new EntitySpellCharge(context.caster.getEntityWorld(), context.caster);
                break;

            case 13: // Mine
                projectile = new EntitySpellMine(context.caster.getEntityWorld(), context.caster);
                break;
        }

        if (projectile != null) {
            projectile.setInfo(context.caster, colorizer, stack);
            projectile.setPosition(this.posX, this.posY, this.posZ);
            projectile.context = context;
            projectile.getEntityWorld().spawnEntity(projectile);
        }
    }
    @Override
    protected void onHit(RayTraceResult raytraceResultIn) {
        super.onHit(raytraceResultIn);
        if (spellBullet != null && context != null) {
            Spell spell = ItemSpellDrive.getSpell(spellBullet);

        }
    }
    @Override
    public boolean isImmune() {
        return true;
    }

    @Override
    public Vec3d objectLocus() {
        return this.getPositionVector();
    }

    @Override
    public void detonate() {

    }
}
