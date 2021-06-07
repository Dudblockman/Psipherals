package com.dudblockman.psipherals.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import vazkii.psi.api.spell.ISpellAcceptor;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.entity.EntitySpellGrenade;
import vazkii.psi.common.entity.EntitySpellProjectile;

import java.util.function.Consumer;

public class EntityPentaSpellGrenade extends EntitySpellGrenade {

    private static final String TAG_BULLET = "bullet";
    private static final DataParameter<ItemStack> BULLET_DATA = EntityDataManager.createKey(EntityPentaSpellGrenade.class, DataSerializers.ITEMSTACK);

    public EntityPentaSpellGrenade(EntityType<? extends ThrowableEntity> type, World worldIn) {
        super(type, worldIn);
    }

    protected EntityPentaSpellGrenade(EntityType<? extends ThrowableEntity> type, World worldIn, LivingEntity throwerIn) {
        super(type, worldIn, throwerIn);
    }

    public EntityPentaSpellGrenade(World world, LivingEntity thrower) {
        super(world, thrower);
    }

    public EntitySpellProjectile setInfo(PlayerEntity player, ItemStack colorizer, ItemStack bullet) {
        super.setInfo(player, colorizer, bullet);
        dataManager.set(BULLET_DATA, bullet.copy());

        return this;
    }

    @Override
    protected void registerData() {
        dataManager.register(BULLET_DATA, ItemStack.EMPTY);
        super.registerData();
    }

    @Override
    public void writeAdditional(CompoundNBT tagCompound) {
        super.writeAdditional(tagCompound);

        CompoundNBT bulletCmp = new CompoundNBT();
        ItemStack bullet = dataManager.get(BULLET_DATA);
        if (!bullet.isEmpty()) {
            bulletCmp = bullet.write(bulletCmp);
        }
        tagCompound.put(TAG_BULLET, bulletCmp);
    }

    @Override
    public void readAdditional(CompoundNBT tagCompound) {
        super.readAdditional(tagCompound);
        CompoundNBT bulletCmp = tagCompound.getCompound(TAG_BULLET);
        ItemStack bullet = ItemStack.read(bulletCmp);
        dataManager.set(BULLET_DATA, bullet);
    }

    @Override
    public void cast(Consumer<SpellContext> callback) {
        Entity thrower = func_234616_v_();

        if (thrower instanceof PlayerEntity) {
            ItemStack spellContainer = dataManager.get(BULLET_DATA);
            if (!spellContainer.isEmpty() && ISpellAcceptor.isContainer(spellContainer)) {
                Spell spell = ISpellAcceptor.acceptor(spellContainer).getSpell();
                if (spell != null) {
                    for (int i = 0; i < 5; i++) {
                        context = new SpellContext().setPlayer((PlayerEntity) thrower).setFocalPoint(this).setSpell(spell).setLoopcastIndex(i);
                        if (callback != null) {
                            callback.accept(context);
                        }
                        if (context != null) {
                            context.cspell.safeExecute(context);
                        }
                    }
                }
            }
        }

        remove();
    }
}
