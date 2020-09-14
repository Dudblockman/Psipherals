package com.dudblockman.psipherals.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.Items;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.entity.EntitySpellProjectile;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.item.tool.IPsimetalTool;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemPsimetalBow extends BowItem implements IPsimetalTool {
    public ItemPsimetalBow(Item.Properties props, String model) {
        super(props.maxDamage(384));
    }

    @Nonnull
    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity playerentity = (PlayerEntity) entityLiving;
            boolean flag = playerentity.abilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
            ItemStack itemstack = playerentity.findAmmo(stack);

            int i = this.getUseDuration(stack) - timeLeft;
            i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, playerentity, i, !itemstack.isEmpty() || flag);
            if (i < 0) return;

            if (!itemstack.isEmpty() || flag) {
                if (itemstack.isEmpty()) {
                    itemstack = new ItemStack(Items.ARROW);
                }

                float f = getArrowVelocity(i);
                if (!((double) f < 0.1D)) {
                    boolean flag1 = playerentity.abilities.isCreativeMode || (itemstack.getItem() instanceof ArrowItem && ((ArrowItem) itemstack.getItem()).isInfinite(itemstack, stack, playerentity));
                    if (!worldIn.isRemote) {
                        ArrowItem arrowitem = (ArrowItem) (itemstack.getItem() instanceof ArrowItem ? itemstack.getItem() : Items.ARROW);
                        AbstractArrowEntity abstractarrowentity = arrowitem.createArrow(worldIn, itemstack, playerentity);
                        abstractarrowentity = customeArrow(abstractarrowentity);
                        abstractarrowentity.shoot(playerentity, playerentity.rotationPitch, playerentity.rotationYaw, 0.0F, f * 3.0F, 1.0F);
                        if (f == 1.0F) {
                            abstractarrowentity.setIsCritical(true);
                        }

                        int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);
                        if (j > 0) {
                            abstractarrowentity.setDamage(abstractarrowentity.getDamage() + (double) j * 0.5D + 0.5D);
                        }

                        int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);
                        if (k > 0) {
                            abstractarrowentity.setKnockbackStrength(k);
                        }

                        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0) {
                            abstractarrowentity.setFire(100);
                        }

                        stack.damageItem(1, playerentity, (p_220009_1_) -> {
                            p_220009_1_.sendBreakAnimation(playerentity.getActiveHand());
                        });
                        if (flag1 || playerentity.abilities.isCreativeMode && (itemstack.getItem() == Items.SPECTRAL_ARROW || itemstack.getItem() == Items.TIPPED_ARROW)) {
                            abstractarrowentity.pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
                        }

                        //Spellcasting Logic here

                        //castSpell(playerentity, stack, new Vec3d(playerentity.getPosX(), playerentity.getPosY(), playerentity.getPosZ()), abstractarrowentity);
                        //if (isEnabled(stack)) {
                            PlayerDataHandler.PlayerData data = PlayerDataHandler.get(playerentity);
                            ItemStack playerCad = PsiAPI.getPlayerCAD(playerentity);

                            if (!playerCad.isEmpty()) {
                                ISocketable sockets = ISocketable.socketable(stack);
                                ItemStack bullet = sockets.getSelectedBullet();
                                ItemCAD.cast(playerentity.getEntityWorld(), playerentity, data, bullet, playerCad, 5, 10, 0.05F, (SpellContext context) -> {
                                    context.tool = stack;
                                });

                                float radiusVal = 0.2f;
                                AxisAlignedBB region = new AxisAlignedBB(playerentity.getPosX() - radiusVal, playerentity.getPosY() + playerentity.getEyeHeight() - radiusVal, playerentity.getPosZ() - radiusVal, playerentity.getPosX() + radiusVal, playerentity.getPosY() + playerentity.getEyeHeight() + radiusVal, playerentity.getPosZ() + radiusVal);

                                List<EntitySpellProjectile> spells = playerentity.world.getEntitiesWithinAABB(EntitySpellProjectile.class, region, (e) -> ((e != null) && (e.context.caster == playerentity) && (e.ticksExisted <= 1)));
                                for (EntitySpellProjectile spell : spells) {
                                    spell.startRiding(abstractarrowentity, true);
                                }

                            }
                        //}

                        worldIn.addEntity(abstractarrowentity);
                    }

                    worldIn.playSound(null, playerentity.getPosX(), playerentity.getPosY(), playerentity.getPosZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
                    if (!flag1 && !playerentity.abilities.isCreativeMode) {
                        itemstack.shrink(1);
                        if (itemstack.isEmpty()) {
                            playerentity.inventory.deleteStack(itemstack);
                        }
                    }

                    playerentity.addStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }
    @Override
    public void castOnBlockBreak(ItemStack itemstack, PlayerEntity player) {}

    @Override
    public void setDamage(ItemStack stack, int damage) {
        if (damage > stack.getMaxDamage()) {
            damage = stack.getDamage();
        }
        super.setDamage(stack, damage);
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, @Nonnull ItemStack newStack, boolean slotChanged) {
        return slotChanged;
    }

    @Override
    public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        IPsimetalTool.regen(stack, entityIn);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return IPsimetalTool.super.initCapabilities(stack, nbt);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World playerIn, List<ITextComponent> tooltip, ITooltipFlag advanced) {
        ITextComponent componentName = ISocketable.getSocketedItemName(stack, "psimisc.none");
        tooltip.add(new TranslationTextComponent("psimisc.spell_selected", componentName));
    }

    @Override
    public boolean getIsRepairable(ItemStack thisStack, @Nonnull ItemStack material) {
        return IPsimetalTool.isRepairableBy(material) || super.getIsRepairable(thisStack, material);
    }
}
