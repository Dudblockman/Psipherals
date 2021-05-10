package com.dudblockman.psipherals.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ICrossbowUser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.spell.*;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.entity.EntitySpellProjectile;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.item.tool.IPsimetalTool;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemPsimetalCrossbow extends CrossbowItem implements IPsimetalTool {

    public ItemPsimetalCrossbow(Properties propertiesIn) {
        super(propertiesIn.maxDamage(326));
    }

    @Override
    public boolean isCrossbow(ItemStack stack) {
        return stack.getItem() instanceof CrossbowItem;
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World worldIn, PlayerEntity playerIn, @Nonnull Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        if (isCharged(itemstack)) {
            fireProjectiles(worldIn, playerIn, handIn, itemstack, func_220013_l(itemstack), 1.0F);
            setCharged(itemstack, false);
            return ActionResult.resultConsume(itemstack);
        } else if (!playerIn.findAmmo(itemstack).isEmpty()) {
            if (!isCharged(itemstack)) {
                this.isLoadingStart = false;
                this.isLoadingMiddle = false;
                playerIn.setActiveHand(handIn);
            }

            return ActionResult.resultConsume(itemstack);
        } else {
            return ActionResult.resultFail(itemstack);
        }
    }

    public static void fireProjectiles(World worldIn, LivingEntity shooter, Hand handIn, ItemStack stack, float velocityIn, float inaccuracyIn) {
        ItemStack crossbow = shooter.getHeldItem(handIn);
        List<ItemStack> list = getChargedProjectiles(stack);
        float[] afloat = getRandomSoundPitches(shooter.getRNG());

        for(int i = 0; i < list.size(); ++i) {
            ItemStack itemstack = list.get(i);
            boolean flag = shooter instanceof PlayerEntity && ((PlayerEntity)shooter).abilities.isCreativeMode;
            if (!itemstack.isEmpty()) {
                ProjectileEntity projectile = null;
                if (i == 0) {
                    projectile = fireProjectile(worldIn, shooter, handIn, stack, itemstack, afloat[i], flag, velocityIn, inaccuracyIn, 0.0F);
                } else if (i == 1) {
                    projectile = fireProjectile(worldIn, shooter, handIn, stack, itemstack, afloat[i], flag, velocityIn, inaccuracyIn, -10.0F);
                } else if (i == 2) {
                    projectile = fireProjectile(worldIn, shooter, handIn, stack, itemstack, afloat[i], flag, velocityIn, inaccuracyIn, 10.0F);
                }
                if (projectile != null && shooter instanceof PlayerEntity){
                    attachSpell((PlayerEntity)shooter, crossbow, projectile);
                }
            }
        }
        if (crossbow.getTag() != null && crossbow.getTag().contains("castBullet")) {
            CompoundNBT stackTag = crossbow.getTag();
            stackTag.remove("castBullet");
            crossbow.setTag(stackTag);
        }

        fireProjectilesAfter(worldIn, shooter, stack);
    }

    private static ProjectileEntity fireProjectile(World worldIn, LivingEntity shooter, Hand handIn, ItemStack crossbow, ItemStack projectile, float soundPitch, boolean isCreativeMode, float velocity, float inaccuracy, float projectileAngle) {
        if (!worldIn.isRemote) {
            boolean flag = projectile.getItem() == Items.FIREWORK_ROCKET;
            ProjectileEntity projectileentity;
            if (flag) {
                projectileentity = new FireworkRocketEntity(worldIn, projectile, shooter, shooter.getPosX(), shooter.getPosYEye() - (double)0.15F, shooter.getPosZ(), true);
            } else {
                projectileentity = createArrow(worldIn, shooter, crossbow, projectile);
                if (isCreativeMode || projectileAngle != 0.0F) {
                    ((AbstractArrowEntity)projectileentity).pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
                }
            }

            if (shooter instanceof ICrossbowUser) {
                ICrossbowUser icrossbowuser = (ICrossbowUser)shooter;
                icrossbowuser.func_230284_a_(icrossbowuser.getAttackTarget(), crossbow, projectileentity, projectileAngle);
            } else {
                Vector3d vector3d1 = shooter.getUpVector(1.0F);
                Quaternion quaternion = new Quaternion(new Vector3f(vector3d1), projectileAngle, true);
                Vector3d vector3d = shooter.getLook(1.0F);
                Vector3f vector3f = new Vector3f(vector3d);
                vector3f.transform(quaternion);
                projectileentity.shoot(vector3f.getX(), vector3f.getY(), vector3f.getZ(), velocity, inaccuracy);
            }

            crossbow.damageItem(flag ? 3 : 1, shooter, (p_220017_1_) -> {
                p_220017_1_.sendBreakAnimation(handIn);
            });
            worldIn.addEntity(projectileentity);
            worldIn.playSound(null, shooter.getPosX(), shooter.getPosY(), shooter.getPosZ(), SoundEvents.ITEM_CROSSBOW_SHOOT, SoundCategory.PLAYERS, 1.0F, soundPitch);
            return projectileentity;
        }
        return null;
    }

    @Override
    public void onPlayerStoppedUsing(@Nonnull ItemStack stack, @Nonnull World worldIn, @Nonnull LivingEntity entityLiving, int timeLeft) {
        int i = this.getUseDuration(stack) - timeLeft;
        float f = getCharge(i, stack);
        if (f >= 1.0F && !isCharged(stack) && hasAmmo(entityLiving, stack) && entityLiving instanceof PlayerEntity) {
            setCharged(stack, true);
            SoundCategory soundcategory = entityLiving instanceof PlayerEntity ? SoundCategory.PLAYERS : SoundCategory.HOSTILE;
            worldIn.playSound(null, entityLiving.getPosX(), entityLiving.getPosY(), entityLiving.getPosZ(), SoundEvents.ITEM_CROSSBOW_LOADING_END, soundcategory, 1.0F, 1.0F / (random.nextFloat() * 0.5F + 1.0F) + 0.2F);
            addSpell((PlayerEntity)entityLiving, stack);
        }
    }

    private static void attachSpell(PlayerEntity player, ItemStack stack, ProjectileEntity projectile) {
        if (ItemCAD.isTruePlayer(player) && stack.getTag() != null && stack.getTag().contains("castBullet") ) {
            CompoundNBT stackTag = stack.getTag();
            CompoundNBT tag = (CompoundNBT) stackTag.get("castBullet");

            ItemStack spellBullet = ItemStack.read(tag);

            PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
            ItemStack playerCad = PsiAPI.getPlayerCAD(player);
            if (!playerCad.isEmpty()) {
                ISpellAcceptor spellContainer = ISpellAcceptor.acceptor(spellBullet);

                ItemCAD.cast(player.getEntityWorld(), player, data, spellBullet, playerCad, 5, 10, 0.05F, (SpellContext context) -> {
                    context.tool = stack;
                }, data.totalPsi);

                float radiusVal = 0.2f;
                AxisAlignedBB region = new AxisAlignedBB(player.getPosX() - radiusVal, player.getPosY() + player.getEyeHeight() - radiusVal, player.getPosZ() - radiusVal, player.getPosX() + radiusVal, player.getPosY() + player.getEyeHeight() + radiusVal, player.getPosZ() + radiusVal);

                List<EntitySpellProjectile> spells = player.world.getEntitiesWithinAABB(EntitySpellProjectile.class, region, (e) -> ((e != null) && (e.context.caster == player) && (e.ticksExisted <= 1) && (e.getRidingEntity() == null)));
                for (EntitySpellProjectile spellentity : spells) {
                    spellentity.startRiding(projectile, true);
                }
            }
        }
    }


    public static void addSpell(PlayerEntity player, ItemStack crossbow) {
        CompoundNBT compoundnbt = crossbow.getOrCreateTag();
        PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
        ItemStack playerCad = PsiAPI.getPlayerCAD(player);
        if (!playerCad.isEmpty()) {
            ISocketable sockets = ISocketable.socketable(crossbow);
            ItemStack bullet = sockets.getSelectedBullet();
            if (!data.overflowed && !bullet.isEmpty() && ISpellAcceptor.hasSpell(bullet) && ItemCAD.isTruePlayer(player)) {
                ISpellAcceptor spellContainer = ISpellAcceptor.acceptor(bullet);
                Spell spell = spellContainer.getSpell();
                SpellContext context = (new SpellContext()).setPlayer(player).setSpell(spell);

                int cost = ItemCAD.getRealCost(playerCad, bullet, context.cspell.metadata.getStat(EnumSpellStat.COST));
                if (EnchantmentHelper.getEnchantments(crossbow).containsKey(Enchantments.MULTISHOT)) {
                    cost *= 3;
                }
                data.deductPsi(cost, 5, true);
                CompoundNBT tag = new CompoundNBT();
                bullet.write(tag);
                compoundnbt.put("castBullet", tag);
            }
        }
        crossbow.setTag(compoundnbt);

    }

    @Override
    public void castOnBlockBreak(ItemStack itemstack, PlayerEntity player) {}

    @Override
    public boolean isEnabled(ItemStack stack) {
        return IPsimetalTool.super.isEnabled(stack);
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
