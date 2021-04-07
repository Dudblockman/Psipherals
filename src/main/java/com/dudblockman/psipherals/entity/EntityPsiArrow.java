package com.dudblockman.psipherals.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import vazkii.psi.common.Psi;
import vazkii.psi.common.entity.EntitySpellProjectile;


public class EntityPsiArrow extends AbstractArrowEntity {

    private static final String TAG_COLORIZER = "colorizer";
    private static final DataParameter<ItemStack> COLORIZER_DATA = EntityDataManager.createKey(EntitySpellProjectile.class, DataSerializers.ITEMSTACK);

    public EntityPsiArrow (EntityType<? extends AbstractArrowEntity> type, World world) {
        super(type, world);
    }
    public EntityPsiArrow(World worldIn, LivingEntity shooter) {
        super(Entities.arrowEntityType, shooter, worldIn);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    protected ItemStack getArrowStack() {
        return ItemStack.EMPTY;
    }


    public EntityPsiArrow setInfo(ItemStack colorizer) {
        dataManager.set(COLORIZER_DATA, colorizer);
        return this;
    }
    @Override
    protected void registerData() {
        super.registerData();
        dataManager.register(COLORIZER_DATA, ItemStack.EMPTY);
    }

    @Override
    public void writeAdditional(CompoundNBT tagCompound) {
        super.writeAdditional(tagCompound);
        CompoundNBT colorizerCmp = new CompoundNBT();
        ItemStack colorizer = dataManager.get(COLORIZER_DATA);
        if (!colorizer.isEmpty()) {
            colorizerCmp = colorizer.write(colorizerCmp);
        }
        tagCompound.put(TAG_COLORIZER, colorizerCmp);
    }

    @Override
    public void readAdditional(CompoundNBT tagCompound) {
        super.readAdditional(tagCompound);

        CompoundNBT colorizerCmp = tagCompound.getCompound(TAG_COLORIZER);
        ItemStack colorizer = ItemStack.read(colorizerCmp);
        dataManager.set(COLORIZER_DATA, colorizer);
    }

    public int getColor () {
        ItemStack colorizer = dataManager.get(COLORIZER_DATA);
        return Psi.proxy.getColorForColorizer(colorizer);
    }
    @Override
    public void updatePassenger(Entity passenger) {
        super.updatePassenger(passenger);
        if (this.isPassenger(passenger) && (passenger instanceof EntitySpellProjectile)) {
            passenger.setPosition(this.getPosX(), this.getPosY(), this.getPosZ());
        }
    }
}
