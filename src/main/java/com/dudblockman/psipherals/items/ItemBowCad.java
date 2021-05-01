package com.dudblockman.psipherals.items;

import com.dudblockman.psipherals.entity.EntityPsiArrow;
import com.dudblockman.psipherals.spell.selector.SelectorAltFire;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.PacketDistributor;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.*;
import vazkii.psi.api.internal.TooltipHelper;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.recipe.ITrickRecipe;
import vazkii.psi.api.spell.PieceGroupAdvancementComplete;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.piece.PieceCraftingTrick;
import vazkii.psi.common.core.handler.ContributorSpellCircleHandler;
import vazkii.psi.common.block.BlockProgrammer;
import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.core.handler.ConfigHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PsiSoundHandler;
import vazkii.psi.common.core.handler.capability.CADData;
import vazkii.psi.common.crafting.ModCraftingRecipes;
import vazkii.psi.common.entity.EntitySpellProjectile;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.lib.LibPieceGroups;
import vazkii.psi.common.network.MessageRegister;
import vazkii.psi.common.network.message.MessageCADDataSync;
import vazkii.psi.common.network.message.MessageVisualEffect;
import vazkii.psi.common.spell.trick.block.PieceTrickBreakBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class ItemBowCad extends BowItem implements ICAD {

    public ItemBowCad(Item.Properties props) {
        super(props);
    }

    private ICADData getCADData(ItemStack stack) {
        return stack.getCapability(PsiAPI.CAD_DATA_CAPABILITY).orElseGet(() -> new CADData(stack));
    }

    private ISocketable getSocketable(ItemStack stack) {
        return stack.getCapability(PsiAPI.SOCKETABLE_CAPABILITY).orElseGet(() -> new CADData(stack));
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        CADData data = new CADData(stack);
        if (nbt != null && nbt.contains("Parent", Constants.NBT.TAG_COMPOUND)) {
            data.deserializeNBT(nbt.getCompound("Parent"));
        }
        return data;
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entityIn, int itemSlot, boolean isSelected) {
        stack.getCapability(PsiAPI.CAD_DATA_CAPABILITY).ifPresent(data -> {
            if (entityIn instanceof ServerPlayerEntity && data.isDirty()) {
                ServerPlayerEntity player = (ServerPlayerEntity) entityIn;
                MessageRegister.sendToPlayer(new MessageCADDataSync(data), player);
                data.markDirty(false);
            }
        });
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity playerentity = (PlayerEntity) entityLiving;

            int i = this.getUseDuration(stack) - timeLeft;
            i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, playerentity, i, true);
            if (i < 0) return;

            float f = getArrowVelocity(i);
            if (!((double) f < 0.1D)) {
                if (!worldIn.isRemote) {
                    PlayerDataHandler.PlayerData data = PlayerDataHandler.get(playerentity);
                    ItemStack playerCad = PsiAPI.getPlayerCAD(playerentity);
                    if (!playerCad.isEmpty()) {
                        AbstractArrowEntity abstractarrowentity = new EntityPsiArrow(worldIn,playerentity).setInfo(getComponentInSlot(playerCad, EnumCADComponent.DYE));
                        abstractarrowentity = customArrow(abstractarrowentity);
                        abstractarrowentity.func_234612_a_(playerentity, playerentity.rotationPitch, playerentity.rotationYaw, 0.0F, f * 3.0F, 1.0F);
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

                        int cost = 150 / (1 + EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, stack));
                        cost += EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) == 0 ? 750 : 0;
                        data.deductPsi(cost, 0, true, false);

                        abstractarrowentity.pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;


                        //Spellcasting Logic here

                        //castSpell(playerentity, stack, new Vec3d(playerentity.getPosX(), playerentity.getPosY(), playerentity.getPosZ()), abstractarrowentity);
                        //if (isEnabled(stack)) {

                        ISocketable sockets = ISocketable.socketable(stack);
                        ItemStack bullet = sockets.getSelectedBullet();
                        ItemCAD.cast(playerentity.getEntityWorld(), playerentity, data, bullet, playerCad, 5, 10, 0.05F, (SpellContext context) -> {
                            context.tool = stack;
                            context.customData.put(SelectorAltFire.ALTFIREKEY, 1);
                        });

                        float radiusVal = 0.2f;
                        AxisAlignedBB region = new AxisAlignedBB(playerentity.getPosX() - radiusVal, playerentity.getPosY() + playerentity.getEyeHeight() - radiusVal, playerentity.getPosZ() - radiusVal, playerentity.getPosX() + radiusVal, playerentity.getPosY() + playerentity.getEyeHeight() + radiusVal, playerentity.getPosZ() + radiusVal);

                        List<EntitySpellProjectile> spells = playerentity.world.getEntitiesWithinAABB(EntitySpellProjectile.class, region, (e) -> ((e != null) && (e.context.caster == playerentity) && (e.ticksExisted <= 1)));
                        for (EntitySpellProjectile spell : spells) {
                            spell.startRiding(abstractarrowentity, true);
                        }
                        worldIn.addEntity(abstractarrowentity);
                    }
                }

                worldIn.playSound(null, playerentity.getPosX(), playerentity.getPosY(), playerentity.getPosZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

                playerentity.addStat(Stats.ITEM_USED.get(this));
            }
        }
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext ctx) {
        World worldIn = ctx.getWorld();
        Hand hand = ctx.getHand();
        BlockPos pos = ctx.getPos();
        PlayerEntity playerIn = ctx.getPlayer();
        ItemStack stack = playerIn.getHeldItem(hand);
        Block block = worldIn.getBlockState(pos).getBlock();
        return block == ModBlocks.programmer ? ((BlockProgrammer) block).setSpell(worldIn, pos, playerIn, stack) : ActionResultType.PASS;
    }

    public boolean castSpell(LivingEntity entityLiving, ItemStack stack) {
        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity playerIn = (PlayerEntity) entityLiving;
            World worldIn = playerIn.getEntityWorld();
            PlayerDataHandler.PlayerData data = PlayerDataHandler.get(playerIn);
            ItemStack playerCad = PsiAPI.getPlayerCAD(playerIn);
            if (playerCad != stack) {
                if (!worldIn.isRemote) {
                    playerIn.sendMessage(new TranslationTextComponent("psimisc.multiple_cads").setStyle(Style.EMPTY.setFormatting(TextFormatting.RED)), Util.DUMMY_UUID);
                }
                return false;
            }
            ISocketable sockets = getSocketable(playerCad);

            ItemStack bullet = sockets.getSelectedBullet();
            if (!getComponentInSlot(playerCad, EnumCADComponent.DYE).isEmpty() && ContributorSpellCircleHandler.isContributor(playerIn.getName().getString().toLowerCase())) {
                ItemStack dyeStack = getComponentInSlot(playerCad, EnumCADComponent.DYE);
                if (!((ICADColorizer) dyeStack.getItem()).getContributorName(dyeStack).equals(playerIn.getName().getString().toLowerCase())) {
                    ((ICADColorizer) dyeStack.getItem()).setContributorName(dyeStack, playerIn.getName().getString());
                    setCADComponent(playerCad, dyeStack);
                }
            }
            boolean did = ItemCAD.cast(worldIn, playerIn, data, bullet, stack, 40, 25, 0.5F, ctx -> ctx.castFrom = Hand.MAIN_HAND);

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
        return false;
    }

    @Override
    public boolean craft(ItemStack cad, PlayerEntity player, PieceCraftingTrick craftingTrick) {
        if (player.world.isRemote) {
            return false;
        }

        List<ItemEntity> items = player.getEntityWorld().getEntitiesWithinAABB(ItemEntity.class,
                player.getBoundingBox().grow(8),
                entity -> entity != null && entity.getDistanceSq(player) <= 8 * 8);

        ItemAxeCad.CraftingWrapper inv = new ItemAxeCad.CraftingWrapper();
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

    public static void setComponent(ItemStack stack, ItemStack componentStack) {
        if (stack.getItem() instanceof ICAD) {
            ((ICAD) stack.getItem()).setCADComponent(stack, componentStack);
        }
    }

    public static ItemStack makeCAD(ItemStack... components) {
        return makeCAD(Arrays.asList(components));
    }

    public static ItemStack makeCADWithAssembly(ItemStack assembly, List<ItemStack> components) {
        ItemStack cad = assembly.getItem() instanceof ICADAssembly ? ((ICADAssembly) assembly.getItem()).createCADStack(assembly, components) : new ItemStack(Items.bowCAD);

        return makeCAD(cad, components);
    }

    public static ItemStack makeCAD(List<ItemStack> components) {
        return makeCAD(new ItemStack(Items.bowCAD), components);
    }

    public static ItemStack makeCAD(ItemStack base, List<ItemStack> components) {
        ItemStack stack = base.copy();
        for (ItemStack component : components) {
            setComponent(stack, component);
        }
        return stack;
    }

    @Override
    public ItemStack getComponentInSlot(ItemStack stack, EnumCADComponent type) {
        String name = TAG_COMPONENT_PREFIX + type.name();
        CompoundNBT cmp = stack.getOrCreateTag().getCompound(name);

        if (cmp.isEmpty()) {
            return ItemStack.EMPTY;
        }

        return ItemStack.read(cmp);
    }

    @Override
    public int getStatValue(ItemStack stack, EnumCADStat stat) {
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

    @Override
    @OnlyIn(Dist.CLIENT)
    public int getSpellColor(ItemStack stack) {
        ItemStack dye = getComponentInSlot(stack, EnumCADComponent.DYE);
        if (!dye.isEmpty() && dye.getItem() instanceof ICADColorizer) {
            return ((ICADColorizer) dye.getItem()).getColor(dye);
        }
        return ICADColorizer.DEFAULT_SPELL_COLOR;
    }

    @Override
    public int getTime(ItemStack stack) {
        return getCADData(stack).getTime();
    }

    @Override
    public void incrementTime(ItemStack stack) {
        ICADData data = getCADData(stack);
        data.setTime(data.getTime() + 1);
    }

    @Override
    public int getStoredPsi(ItemStack stack) {
        int maxPsi = getStatValue(stack, EnumCADStat.OVERFLOW);

        return Math.min(getCADData(stack).getBattery(), maxPsi);
    }

    @Override
    public void regenPsi(ItemStack stack, int psi) {
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

    @Override
    public int consumePsi(ItemStack stack, int psi) {
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

    @Override
    public int getMemorySize(ItemStack stack) {
        int sockets = getStatValue(stack, EnumCADStat.SOCKETS);
        if (sockets == -1) {
            return 0xFF;
        }
        return sockets / 3;
    }

    @Override
    public void setStoredVector(ItemStack stack, int memorySlot, Vector3 vec) throws SpellRuntimeException {
        int size = getMemorySize(stack);
        if (memorySlot < 0 || memorySlot >= size) {
            throw new SpellRuntimeException(SpellRuntimeException.MEMORY_OUT_OF_BOUNDS);
        }
        getCADData(stack).setSavedVector(memorySlot, vec);
    }

    @Override
    public Vector3 getStoredVector(ItemStack stack, int memorySlot) throws SpellRuntimeException {
        int size = getMemorySize(stack);
        if (memorySlot < 0 || memorySlot >= size) {
            throw new SpellRuntimeException(SpellRuntimeException.MEMORY_OUT_OF_BOUNDS);
        }
        return getCADData(stack).getSavedVector(memorySlot);
    }

    @Override
    public int getHarvestLevel(ItemStack stack, ToolType tool, @Nullable PlayerEntity player, @Nullable BlockState blockState) {
        if (!PieceTrickBreakBlock.doingHarvestCheck.get()) {
            return -1;
        }
        int level = super.getHarvestLevel(stack, tool, player, blockState);
        return level < 0 ? -1 : Math.max(level, ConfigHandler.COMMON.cadHarvestLevel.get());
    }

    @Override
    public void fillItemGroup(@Nonnull ItemGroup tab, @Nonnull NonNullList<ItemStack> subItems) {
        if (!isInGroup(tab)) {
            return;
        }

        // Psimetal CAD
        subItems.add(makeCAD(new ItemStack(ModItems.cadAssemblyPsimetal),
                new ItemStack(ModItems.cadCoreOverclocked),
                new ItemStack(ModItems.cadSocketSignaling),
                new ItemStack(ModItems.cadBatteryExtended)));
        // Ebony Psimetal CAD
        subItems.add(makeCAD(new ItemStack(ModItems.cadAssemblyEbony),
                new ItemStack(ModItems.cadCoreHyperClocked),
                new ItemStack(ModItems.cadSocketTransmissive),
                new ItemStack(ModItems.cadBatteryUltradense)));
        // Ivory Psimetal CAD
        subItems.add(makeCAD(new ItemStack(ModItems.cadAssemblyIvory),
                new ItemStack(ModItems.cadCoreHyperClocked),
                new ItemStack(ModItems.cadSocketTransmissive),
                new ItemStack(ModItems.cadBatteryUltradense)));
        // Creative CAD
        subItems.add(makeCAD(new ItemStack(ModItems.cadAssemblyCreative),
                new ItemStack(ModItems.cadCoreHyperClocked),
                new ItemStack(ModItems.cadSocketTransmissive),
                new ItemStack(ModItems.cadBatteryUltradense)));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World playerin, List<ITextComponent> tooltip, ITooltipFlag advanced) {
        TooltipHelper.tooltipIfShift(tooltip, () -> {
            ITextComponent componentName = ISocketable.getSocketedItemName(stack, "psimisc.none");
            tooltip.add(new TranslationTextComponent("psimisc.spell_selected", componentName));

            for (EnumCADComponent componentType : EnumCADComponent.class.getEnumConstants()) {
                ItemStack componentStack = getComponentInSlot(stack, componentType);
                ITextComponent name = new TranslationTextComponent("psimisc.none");
                if (!componentStack.isEmpty()) {
                    name = componentStack.getDisplayName();
                }

                IFormattableTextComponent componentTypeName = new TranslationTextComponent(componentType.getName()).mergeStyle(TextFormatting.GREEN);
                tooltip.add(componentTypeName.appendString(": ").append(name));

                for (EnumCADStat stat : EnumCADStat.class.getEnumConstants()) {
                    if (stat.getSourceType() == componentType) {
                        String shrt = stat.getName();
                        int statVal = getStatValue(stack, stat);
                        String statValStr = statVal == -1 ? "\u221E" : "" + statVal;

                        tooltip.add(new TranslationTextComponent(shrt).mergeStyle(TextFormatting.AQUA).appendString(": " + statValStr));
                    }
                }
            }
        });
    }

    @Nonnull
    @Override
    public Rarity getRarity(ItemStack stack) {
        return Rarity.RARE;
    }
}
