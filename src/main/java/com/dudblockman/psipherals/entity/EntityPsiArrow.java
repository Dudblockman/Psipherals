package com.dudblockman.psipherals.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.world.World;
import vazkii.psi.api.spell.ISpellImmune;

public class EntityPsiArrow extends EntityTippedArrow implements ISpellImmune {
    public EntityPsiArrow(World worldIn) {
        super(worldIn);
    }
    public EntityPsiArrow(World worldIn, EntityLivingBase shooter) {
        super(worldIn, shooter);
    }
    public  EntityPsiArrow(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    @Override
    public boolean isImmune() {
        return true;
    }
}
