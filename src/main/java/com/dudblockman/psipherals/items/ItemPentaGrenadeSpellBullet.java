package com.dudblockman.psipherals.items;

import com.dudblockman.psipherals.entity.EntityPentaSpellGrenade;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.entity.EntitySpellProjectile;
import vazkii.psi.common.item.ItemGrenadeSpellBullet;

import java.util.ArrayList;

public class ItemPentaGrenadeSpellBullet extends ItemGrenadeSpellBullet {
    public ItemPentaGrenadeSpellBullet(Properties properties) {
        super(properties);
    }

    @Override
    public ArrayList<Entity> castSpell(ItemStack stack, SpellContext context) {
        ItemStack cad = PsiAPI.getPlayerCAD(context.caster);
        ItemStack colorizer = ((ICAD) cad.getItem()).getComponentInSlot(cad, EnumCADComponent.DYE);

        EntitySpellProjectile projectile = new EntityPentaSpellGrenade(context.caster.getEntityWorld(), context.caster);
        projectile.setInfo(context.caster, colorizer, stack);
        projectile.context = context;
        projectile.getEntityWorld().addEntity(projectile);
        ArrayList<Entity> spellEntities = new ArrayList<>();
        spellEntities.add(projectile);
        return spellEntities;
    }

    @Override
    public double getCostModifier(ItemStack stack) {
        return 4.00;
    }

}
