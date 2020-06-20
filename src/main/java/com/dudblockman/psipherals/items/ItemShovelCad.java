package com.dudblockman.psipherals.items;

import com.dudblockman.psipherals.util.libs.AdvPsimetalToolMaterial;
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
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.*;
import vazkii.psi.api.internal.PsiRenderHelper;
import vazkii.psi.api.internal.TooltipHelper;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.recipe.ITrickRecipe;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.piece.PieceCraftingTrick;
import vazkii.psi.client.core.handler.ContributorSpellCircleHandler;
import vazkii.psi.common.Psi;
import vazkii.psi.common.block.BlockProgrammer;
import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.core.handler.ConfigHandler;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PsiSoundHandler;
import vazkii.psi.common.core.handler.capability.CADData;
import vazkii.psi.common.crafting.ModCraftingRecipes;
import vazkii.psi.common.item.ItemCAD;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.item.tool.IPsimetalTool;
import vazkii.psi.common.lib.LibPieceGroups;
import vazkii.psi.common.network.MessageRegister;
import vazkii.psi.common.network.message.MessageCADDataSync;
import vazkii.psi.common.network.message.MessageVisualEffect;
import vazkii.psi.common.spell.trick.block.PieceTrickBreakBlock;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemShovelCad extends ShovelItem implements ICAD {

    private static final String TAG_BULLET_PREFIX = "bullet";
    private static final String TAG_SELECTED_SLOT = "selectedSlot";

    // Legacy tags
    private static final String TAG_TIME_LEGACY = "time";
    private static final String TAG_STORED_PSI_LEGACY = "storedPsi";

    private static final String TAG_X_LEGACY = "x";
    private static final String TAG_Y_LEGACY = "y";
    private static final String TAG_Z_LEGACY = "z";
    private static final Pattern VECTOR_PREFIX_PATTERN = Pattern.compile("^storedVector(\\d+)$");

    private static final Pattern FAKE_PLAYER_PATTERN = Pattern.compile("^(?:\\[.*])|(?:ComputerCraft)$");

    public ItemShovelCad(Item.Properties props) {
        super(new AdvPsimetalToolMaterial(), 5.0F, -3.0F, props);
    }

    private ICADData getCADData(ItemStack stack) {
        return stack.getCapability(PsiAPI.CAD_DATA_CAPABILITY).orElseGet(() -> new CADData(stack));
    }

    private ISocketable getSocketable(ItemStack stack) {
        return stack.getCapability(PsiAPI.SOCKETABLE_CAPABILITY).orElseGet(() -> new CADData(stack));
    }

    @Override
    public boolean hitEntity(ItemStack itemstack, LivingEntity target, @Nonnull LivingEntity attacker) {
        super.hitEntity(itemstack, target, attacker);

        if (attacker instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) attacker;

            PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
            ItemStack playerCad = PsiAPI.getPlayerCAD(player);

            if (!playerCad.isEmpty()) {
                ItemStack bullet = ISocketable.socketable(itemstack).getSelectedBullet();
                ItemCAD.cast(player.getEntityWorld(), player, data, bullet, playerCad, 5, 10, 0.05F,
                        (SpellContext context) -> {
                            context.attackedEntity = target;
                            context.tool = itemstack;
                        });
            }

            int cost = 150 / (1 + EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, itemstack));
            data.deductPsi(cost * 2, 0, true, false);
        }
        return true;
    }

    public void castOnBlockBreak(ItemStack itemstack, PlayerEntity player) {

        PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
        ItemStack playerCad = PsiAPI.getPlayerCAD(player);

        if (!playerCad.isEmpty()) {
            ISocketable sockets = ISocketable.socketable(itemstack);
            ItemStack bullet = sockets.getSelectedBullet();
            ItemCAD.cast(player.getEntityWorld(), player, data, bullet, playerCad, 5, 10, 0.05F, (SpellContext context) -> {
                context.tool = itemstack;
                context.positionBroken = IPsimetalTool.raytraceFromEntity(player.getEntityWorld(), player, RayTraceContext.FluidMode.NONE, player.getAttributes().getAttributeInstance(PlayerEntity.REACH_DISTANCE).getValue());
            });
        }
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, PlayerEntity player) {
        super.onBlockStartBreak(itemstack, pos, player);

        castOnBlockBreak(itemstack, player);

        return false;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        if ((double) state.getBlockHardness(worldIn, pos) != 0.0D) {
            if (entityLiving instanceof PlayerEntity) {
                PlayerEntity entityplayer = (PlayerEntity) entityLiving;
                PlayerDataHandler.PlayerData data = PlayerDataHandler.get(entityplayer);
                int cost = 150 / (1 + EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, stack));
                data.deductPsi(cost, 0, true, false);
            }
            return true;
        }

        return true;
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
        CompoundNBT compound = stack.getOrCreateTag();

        stack.getCapability(PsiAPI.CAD_DATA_CAPABILITY).ifPresent(data -> {
            if (compound.contains(TAG_TIME_LEGACY, Constants.NBT.TAG_ANY_NUMERIC)) {
                data.setTime(compound.getInt(TAG_TIME_LEGACY));
                data.markDirty(true);
                compound.remove(TAG_TIME_LEGACY);
            }

            if (compound.contains(TAG_STORED_PSI_LEGACY, Constants.NBT.TAG_ANY_NUMERIC)) {
                data.setBattery(compound.getInt(TAG_STORED_PSI_LEGACY));
                data.markDirty(true);
                compound.remove(TAG_STORED_PSI_LEGACY);
            }

            Set<String> keys = new HashSet<>(compound.keySet());

            for (String key : keys) {
                Matcher matcher = VECTOR_PREFIX_PATTERN.matcher(key);
                if (matcher.find()) {
                    CompoundNBT vec = compound.getCompound(key);
                    compound.remove(key);
                    int memory = Integer.parseInt(matcher.group(1));
                    Vector3 vector = new Vector3(vec.getDouble(TAG_X_LEGACY),
                            vec.getDouble(TAG_Y_LEGACY),
                            vec.getDouble(TAG_Z_LEGACY));
                    data.setSavedVector(memory, vector);
                }
            }

            if (entityIn instanceof ServerPlayerEntity && data.isDirty()) {
                ServerPlayerEntity player = (ServerPlayerEntity) entityIn;
                MessageRegister.sendToPlayer(new MessageCADDataSync(data), player);
                data.markDirty(false);
            }
        });
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

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, @Nonnull Hand hand) {
        ItemStack itemStackIn = playerIn.getHeldItem(hand);
        PlayerDataHandler.PlayerData data = PlayerDataHandler.get(playerIn);
        ItemStack playerCad = PsiAPI.getPlayerCAD(playerIn);
        if (playerCad != itemStackIn) {
            if (!worldIn.isRemote) {
                playerIn.sendMessage(new TranslationTextComponent("psimisc.multiple_cads").setStyle(new Style().setColor(TextFormatting.RED)));
            }
            return new ActionResult<>(ActionResultType.SUCCESS, itemStackIn);
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
        boolean did = cast(worldIn, playerIn, data, bullet, itemStackIn, 40, 25, 0.5F, ctx -> ctx.castFrom = hand);

        if (!data.overflowed && bullet.isEmpty() && craft(playerCad, playerIn, null)) {
            worldIn.playSound(null, playerIn.getPosX(), playerIn.getPosY(), playerIn.getPosZ(), PsiSoundHandler.cadShoot, SoundCategory.PLAYERS, 0.5F, (float) (0.5 + Math.random() * 0.5));
            data.deductPsi(100, 60, true);

            if (!data.hasAdvancement(LibPieceGroups.FAKE_LEVEL_PSIDUST)) {
                MinecraftForge.EVENT_BUS.post(new PieceGroupAdvancementComplete(null, playerIn, LibPieceGroups.FAKE_LEVEL_PSIDUST));
            }
            did = true;
        }

        return new ActionResult<>(did ? ActionResultType.PASS : ActionResultType.PASS, itemStackIn);
    }

    public static boolean cast(World world, PlayerEntity player, PlayerDataHandler.PlayerData data, ItemStack bullet, ItemStack cad, int cd, int particles, float sound, Consumer<SpellContext> predicate) {
        if (!data.overflowed && data.getAvailablePsi() > 0 && !cad.isEmpty() && !bullet.isEmpty() && ISpellAcceptor.hasSpell(bullet) && isTruePlayer(player)) {
            ISpellAcceptor spellContainer = ISpellAcceptor.acceptor(bullet);
            Spell spell = spellContainer.getSpell();
            SpellContext context = new SpellContext().setPlayer(player).setSpell(spell);
            if (predicate != null) {
                predicate.accept(context);
            }

            if (context.isValid()) {
                if (context.cspell.metadata.evaluateAgainst(cad)) {
                    int cost = getRealCost(cad, bullet, context.cspell.metadata.stats.get(EnumSpellStat.COST));
                    PreSpellCastEvent event = new PreSpellCastEvent(cost, sound, particles, cd, spell, context, player, data, cad, bullet);
                    if (MinecraftForge.EVENT_BUS.post(event)) {
                        String cancelMessage = event.getCancellationMessage();
                        if (cancelMessage != null && !cancelMessage.isEmpty()) {
                            player.sendMessage(new TranslationTextComponent(cancelMessage).setStyle(new Style().setColor(TextFormatting.RED)));
                        }
                        return false;
                    }

                    cd = event.getCooldown();
                    particles = event.getParticles();
                    sound = event.getSound();
                    cost = event.getCost();

                    spell = event.getSpell();
                    context = event.getContext();

                    if (cost > 0) {
                        data.deductPsi(cost, cd, true);
                    }

                    if (cost != 0 && sound > 0) {
                        if (!world.isRemote) {
                            world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), PsiSoundHandler.cadShoot, SoundCategory.PLAYERS, sound, (float) (0.5 + Math.random() * 0.5));
                        } else {
                            int color = Psi.proxy.getColorForCAD(cad);
                            float r = PsiRenderHelper.r(color) / 255F;
                            float g = PsiRenderHelper.g(color) / 255F;
                            float b = PsiRenderHelper.b(color) / 255F;
                            for (int i = 0; i < particles; i++) {
                                double x = player.getPosX() + (Math.random() - 0.5) * 2.1 * player.getWidth();
                                double y = player.getPosY() - player.getYOffset();
                                double z = player.getPosZ() + (Math.random() - 0.5) * 2.1 * player.getWidth();
                                float grav = -0.15F - (float) Math.random() * 0.03F;
                                Psi.proxy.sparkleFX(x, y, z, r, g, b, grav, 0.25F, 15);
                            }

                            double x = player.getPosX();
                            double y = player.getPosY() + player.getEyeHeight() - 0.1;
                            double z = player.getPosZ();
                            Vector3 lookOrig = new Vector3(player.getLookVec());
                            for (int i = 0; i < 25; i++) {
                                Vector3 look = lookOrig.copy();
                                double spread = 0.25;
                                look.x += (Math.random() - 0.5) * spread;
                                look.y += (Math.random() - 0.5) * spread;
                                look.z += (Math.random() - 0.5) * spread;
                                look.normalize().multiply(0.15);

                                Psi.proxy.sparkleFX(x, y, z, r, g, b, (float) look.x, (float) look.y, (float) look.z, 0.3F, 5);
                            }
                        }
                    }

                    if (!world.isRemote) {
                        spellContainer.castSpell(context);
                    }
                    MinecraftForge.EVENT_BUS.post(new SpellCastEvent(spell, context, player, data, cad, bullet));
                    return true;
                } else if (!world.isRemote) {
                    player.sendMessage(new TranslationTextComponent("psimisc.weak_cad").setStyle(new Style().setColor(TextFormatting.RED)));
                }
            }
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

    protected static class CraftingWrapper extends RecipeWrapper {
        CraftingWrapper() {
            super(new ItemStackHandler(1));
        }

        void setStack(ItemStack stack) {
            inv.setStackInSlot(0, stack);
        }
    }

    public static int getRealCost(ItemStack stack, ItemStack bullet, int cost) {
        if (!stack.isEmpty() && stack.getItem() instanceof ICAD) {
            int eff = ((ICAD) stack.getItem()).getStatValue(stack, EnumCADStat.EFFICIENCY);
            if (eff == -1) {
                return -1;
            }
            if (eff == 0) {
                return cost;
            }

            double effPercentile = (double) eff / 100;
            double procCost = cost / effPercentile;
            if (!bullet.isEmpty() && ISpellAcceptor.isContainer(bullet)) {
                procCost *= ISpellAcceptor.acceptor(bullet).getCostModifier();
            }

            return (int) procCost;
        }

        return cost;
    }

    public static boolean isTruePlayer(Entity e) {
        if (!(e instanceof PlayerEntity)) {
            return false;
        }

        PlayerEntity player = (PlayerEntity) e;

        String name = player.getName().getString();
        return !(player instanceof FakePlayer || FAKE_PLAYER_PATTERN.matcher(name).matches());
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
        ItemStack cad = assembly.getItem() instanceof ICADAssembly ? ((ICADAssembly) assembly.getItem()).createCADStack(assembly, components) : new ItemStack(Items.axeCAD);

        return makeCAD(cad, components);
    }

    public static ItemStack makeCAD(List<ItemStack> components) {
        return makeCAD(new ItemStack(Items.axeCAD), components);
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

        // Iron Psimetal CAD
        subItems.add(makeCAD(new ItemStack(Items.shovelAssemblyIron),
                new ItemStack(ModItems.cadCoreBasic),
                new ItemStack(ModItems.cadSocketBasic),
                new ItemStack(ModItems.cadBatteryBasic)));
        // Gold Psimetal CAD
        subItems.add(makeCAD(new ItemStack(Items.shovelAssemblyGold),
                new ItemStack(ModItems.cadCoreBasic),
                new ItemStack(ModItems.cadSocketBasic),
                new ItemStack(ModItems.cadBatteryBasic)));
        // Psimetal CAD
        subItems.add(makeCAD(new ItemStack(Items.shovelAssemblyPsimetal),
                new ItemStack(ModItems.cadCoreOverclocked),
                new ItemStack(ModItems.cadSocketSignaling),
                new ItemStack(ModItems.cadBatteryExtended)));
        // Ebony Psimetal CAD
        subItems.add(makeCAD(new ItemStack(Items.shovelAssemblyEbonyPsimetal),
                new ItemStack(ModItems.cadCoreHyperClocked),
                new ItemStack(ModItems.cadSocketTransmissive),
                new ItemStack(ModItems.cadBatteryUltradense)));
        // Ivory Psimetal CAD
        subItems.add(makeCAD(new ItemStack(Items.shovelAssemblyIvoryPsimetal),
                new ItemStack(ModItems.cadCoreHyperClocked),
                new ItemStack(ModItems.cadSocketTransmissive),
                new ItemStack(ModItems.cadBatteryUltradense)));
        // Creative CAD
        subItems.add(makeCAD(new ItemStack(Items.shovelAssemblyCreative),
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

                ITextComponent componentTypeName = new TranslationTextComponent(componentType.getName()).applyTextStyle(TextFormatting.GREEN);
                tooltip.add(componentTypeName.appendText(": ").appendSibling(name));

                for (EnumCADStat stat : EnumCADStat.class.getEnumConstants()) {
                    if (stat.getSourceType() == componentType) {
                        String shrt = stat.getName();
                        int statVal = getStatValue(stack, stat);
                        String statValStr = statVal == -1 ? "\u221E" : "" + statVal;

                        tooltip.add(new TranslationTextComponent(shrt).applyTextStyle(TextFormatting.AQUA).appendText(": " + statValStr));
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
