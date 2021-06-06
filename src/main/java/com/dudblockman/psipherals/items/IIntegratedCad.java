package com.dudblockman.psipherals.items;

import com.dudblockman.psipherals.spell.selector.SelectorAltFire;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.Util;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.*;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.recipe.ITrickRecipe;
import vazkii.psi.api.spell.PieceGroupAdvancementComplete;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.piece.PieceCraftingTrick;
import vazkii.psi.common.core.handler.ContributorSpellCircleHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PsiSoundHandler;
import vazkii.psi.common.core.handler.capability.CADData;
import vazkii.psi.common.crafting.ModCraftingRecipes;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.item.tool.IPsimetalTool;
import vazkii.psi.common.lib.LibPieceGroups;
import vazkii.psi.common.network.MessageRegister;
import vazkii.psi.common.network.message.MessageVisualEffect;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface IIntegratedCad extends ICAD {

    default void repairTool(ItemStack itemstack, Entity user) {
        int damage = itemstack.getDamage();
        if (user instanceof PlayerEntity && damage > 0) {
            PlayerEntity player = (PlayerEntity) user;
            PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
            int cost = 150 / (1 + EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, itemstack));
            if ((float)data.getAvailablePsi() / (float)data.getTotalPsi() > 0.5F) {
                int repaired = 0;
                do {
                    repaired++;
                } while ((repaired < damage) && (data.getAvailablePsi() - cost * repaired > (float)data.getTotalPsi() * 0.5F));
                data.deductPsi(cost * repaired, 0, true, false);
                itemstack.setDamage(itemstack.getDamage() - repaired);
            }
        }

    }

    boolean isMelee();

    default void castOnAttackEntity(ItemStack itemstack, LivingEntity target, @Nonnull LivingEntity attacker) {
        if (!isMelee()) {
            return;
        }
        if (attacker instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) attacker;

            PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
            ItemStack playerCad = PsiAPI.getPlayerCAD(player);

            if (!playerCad.isEmpty()) {
                ItemStack bullet = ISocketable.socketable(itemstack).getSelectedBullet();
                ItemCAD.cast(player.getEntityWorld(), player, data, bullet, playerCad, 5, 10, 0.05F,
                        (SpellContext context) -> {
                            context.customData.put(SelectorAltFire.ALTFIREKEY, 2);
                            context.attackedEntity = target;
                            context.tool = itemstack;
                        });
            }

        }
    }

    boolean isTool();

    default void castOnBlockBreak(ItemStack itemstack, PlayerEntity player) {
        if (!isTool()) {
            return;
        }
        PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
        ItemStack playerCad = PsiAPI.getPlayerCAD(player);

        if (!playerCad.isEmpty()) {
            ISocketable sockets = ISocketable.socketable(itemstack);
            ItemStack bullet = sockets.getSelectedBullet();
            ItemCAD.cast(player.getEntityWorld(), player, data, bullet, playerCad, 5, 10, 0.05F, (SpellContext context) -> {
                context.tool = itemstack;
                context.customData.put(SelectorAltFire.ALTFIREKEY, 1);
                context.positionBroken = IPsimetalTool.raytraceFromEntity(player.getEntityWorld(), player, RayTraceContext.FluidMode.NONE, player.getAttributeManager().getAttributeValue(ForgeMod.REACH_DISTANCE.get()));
            });
        }
    }

    default boolean normalCast(World worldIn, PlayerEntity playerIn, @Nonnull Hand hand) {
        ItemStack itemStackIn = playerIn.getHeldItem(hand);
        PlayerDataHandler.PlayerData data = PlayerDataHandler.get(playerIn);
        ItemStack playerCad = PsiAPI.getPlayerCAD(playerIn);
        if (playerCad != itemStackIn) {
            if (!worldIn.isRemote) {
                playerIn.sendMessage(new TranslationTextComponent("psimisc.multiple_cads").setStyle(Style.EMPTY.setFormatting(TextFormatting.RED)), Util.DUMMY_UUID);
            }
            return false;
        }
        ISocketable sockets = IIntegratedCad.getSocketable(playerCad);

        ItemStack bullet = sockets.getSelectedBullet();
        if (!getComponentInSlot(playerCad, EnumCADComponent.DYE).isEmpty() && ContributorSpellCircleHandler.isContributor(playerIn.getName().getString().toLowerCase())) {
            ItemStack dyeStack = getComponentInSlot(playerCad, EnumCADComponent.DYE);
            if (!((ICADColorizer) dyeStack.getItem()).getContributorName(dyeStack).equals(playerIn.getName().getString().toLowerCase())) {
                ((ICADColorizer) dyeStack.getItem()).setContributorName(dyeStack, playerIn.getName().getString());
                setCADComponent(playerCad, dyeStack);
            }
        }
        boolean did = ItemCAD.cast(worldIn, playerIn, data, bullet, itemStackIn, 40, 25, 0.5F, ctx -> ctx.castFrom = hand).isPresent();

        if (!data.overflowed && bullet.isEmpty() && craft(playerCad, playerIn, null)) {
            worldIn.playSound(null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), PsiSoundHandler.cadShoot, SoundCategory.PLAYERS, 0.5F, (float) (0.5 + Math.random() * 0.5));
            data.deductPsi(100, 60, true);

            if (!data.hasAdvancement(LibPieceGroups.FAKE_LEVEL_PSIDUST)) {
                MinecraftForge.EVENT_BUS.post(new PieceGroupAdvancementComplete(null, playerIn, LibPieceGroups.FAKE_LEVEL_PSIDUST));
            }
            did = true;
        }
        return did;
    }

    static ICADData getCADData(ItemStack stack) {
        return stack.getCapability(PsiAPI.CAD_DATA_CAPABILITY).orElseGet(() -> new CADData(stack));
    }

    static ISocketable getSocketable(ItemStack stack) {
        return stack.getCapability(PsiAPI.SOCKETABLE_CAPABILITY).orElseGet(() -> new CADData(stack));
    }

    default ItemStack getComponentInSlot(ItemStack stack, EnumCADComponent type) {
        String name = TAG_COMPONENT_PREFIX + type.name();
        CompoundNBT cmp = stack.getOrCreateTag().getCompound(name);

        if (cmp.isEmpty()) {
            return ItemStack.EMPTY;
        }

        return ItemStack.read(cmp);
    }

    default int getStatValue(ItemStack stack, EnumCADStat stat) {
        int statValue = 0;
        ItemStack componentStack = getComponentInSlot(stack, stat.getSourceType());
        if (!componentStack.isEmpty() && componentStack.getItem() instanceof ICADComponent) {
            ICADComponent component = (ICADComponent) componentStack.getItem();
            statValue = component.getCADStatValue(componentStack, stat);
        }

        CADStatEvent event = new CADStatEvent(stat, stack, componentStack, statValue);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getStatValue();
    }

    default int getStoredPsi(ItemStack stack) {
        int maxPsi = getStatValue(stack, EnumCADStat.OVERFLOW);

        return Math.min(getCADData(stack).getBattery(), maxPsi);
    }

    default void regenPsi(ItemStack stack, int psi) {
        int maxPsi = getStatValue(stack, EnumCADStat.OVERFLOW);
        if (maxPsi == -1) {
            return;
        }

        int currPsi = getStoredPsi(stack);
        int endPsi = Math.min(currPsi + psi, maxPsi);

        if (endPsi != currPsi) {
            ICADData data = getCADData(stack);
            data.setBattery(endPsi);
            data.markDirty(true);
        }
    }

    default int consumePsi(ItemStack stack, int psi) {
        if (psi == 0) {
            return 0;
        }

        int currPsi = getStoredPsi(stack);

        if (currPsi == -1) {
            return 0;
        }

        ICADData data = getCADData(stack);

        if (currPsi >= psi) {
            data.setBattery(currPsi - psi);
            data.markDirty(true);
            return 0;
        }

        data.setBattery(0);
        data.markDirty(true);
        return psi - currPsi;
    }

    default int getMemorySize(ItemStack stack) {
        int sockets = getStatValue(stack, EnumCADStat.SOCKETS);
        if (sockets == -1) {
            return 0xFF;
        }
        return sockets / 3;
    }

    default void setStoredVector(ItemStack stack, int memorySlot, Vector3 vec) throws SpellRuntimeException {
        int size = getMemorySize(stack);
        if (memorySlot < 0 || memorySlot >= size) {
            throw new SpellRuntimeException(SpellRuntimeException.MEMORY_OUT_OF_BOUNDS);
        }
        getCADData(stack).setSavedVector(memorySlot, vec);
    }

    default Vector3 getStoredVector(ItemStack stack, int memorySlot) throws SpellRuntimeException {
        int size = getMemorySize(stack);
        if (memorySlot < 0 || memorySlot >= size) {
            throw new SpellRuntimeException(SpellRuntimeException.MEMORY_OUT_OF_BOUNDS);
        }
        return getCADData(stack).getSavedVector(memorySlot);
    }

    default int getTime(ItemStack stack) {
        return getCADData(stack).getTime();
    }

    default void incrementTime(ItemStack stack) {
        ICADData data = getCADData(stack);
        data.setTime(data.getTime() + 1);
    }


    @OnlyIn(Dist.CLIENT)
    default int getSpellColor(ItemStack stack) {
        ItemStack dye = getComponentInSlot(stack, EnumCADComponent.DYE);
        if (!dye.isEmpty() && dye.getItem() instanceof ICADColorizer) {
            return ((ICADColorizer) dye.getItem()).getColor(dye);
        }
        return ICADColorizer.DEFAULT_SPELL_COLOR;
    }

    default boolean craft(ItemStack cad, PlayerEntity player, PieceCraftingTrick craftingTrick) {
        if (player.world.isRemote) {
            return false;
        }

        List<ItemEntity> items = player.getEntityWorld().getEntitiesWithinAABB(ItemEntity.class,
                player.getBoundingBox().grow(8),
                entity -> entity != null && entity.getDistanceSq(player) <= 8 * 8);

        CraftingWrapper inv = new CraftingWrapper();
        boolean did = false;
        for (ItemEntity item : items) {
            ItemStack stack = item.getItem();
            inv.setStack(stack);
            Predicate<ITrickRecipe> predicate = r -> r.getPiece() == null;
            if (craftingTrick != null) {
                predicate = r -> r.getPiece() == null || r.getPiece().canCraft(craftingTrick);
            }

            Optional<ITrickRecipe> recipe = player.world.getRecipeManager().getRecipe(ModCraftingRecipes.TRICK_RECIPE_TYPE, inv, player.world)
                    .filter(predicate);
            if (recipe.isPresent()) {
                ItemStack outCopy = recipe.get().getRecipeOutput().copy();
                outCopy.setCount(stack.getCount());
                item.setItem(outCopy);
                did = true;
                MessageVisualEffect msg = new MessageVisualEffect(ICADColorizer.DEFAULT_SPELL_COLOR,
                        item.getPosX(), item.getPosY(), item.getPosZ(), item.getWidth(), item.getHeight(), item.getYOffset(),
                        MessageVisualEffect.TYPE_CRAFT);
                MessageRegister.HANDLER.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> item), msg);
            }
        }

        return did;
    }

    class CraftingWrapper extends RecipeWrapper {
        CraftingWrapper() {
            super(new ItemStackHandler(1));
        }

        void setStack(ItemStack stack) {
            inv.setStackInSlot(0, stack);
        }
    }
}
