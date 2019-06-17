package com.dudblockman.psipherals.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import vazkii.psi.api.spell.ISpellImmune;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.detonator.IDetonationHandler;
import vazkii.psi.common.item.ItemSpellBullet;

public class EntityPsiArrow extends EntityTippedArrow implements ISpellImmune, IDetonationHandler {
    ItemSpellBullet spell;
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
    @Override
    protected void onHit(RayTraceResult raytraceResultIn) {
        super.onHit(raytraceResultIn);
        if (spell != null && context != null) {
            spell.castSpell((ItemStack) spell, context);
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
