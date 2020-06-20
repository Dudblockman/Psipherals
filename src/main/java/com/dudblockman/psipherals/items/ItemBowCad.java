/*package com.dudblockman.psipherals.items;

import com.dudblockman.psipherals.entity.EntityPsiArrow;
import com.dudblockman.psipherals.util.libs.ItemMaterials;
import com.teamwizardry.librarianlib.features.base.item.ItemModBow;
import com.teamwizardry.librarianlib.features.helpers.NBTHelper;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemGroup;
import net.minecraft.enchantment.UnbreakingEnchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.util.SoundEvents;
import net.minecraft.item.Rarity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.arl.interf.IItemColorProvider;
import vazkii.arl.network.NetworkHandler;
import vazkii.arl.util.TooltipHandler;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.*;
import vazkii.psi.api.internal.PsiRenderHelper;
import vazkii.psi.api.internal.TooltipHelper;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.common.Psi;
import vazkii.psi.common.block.BlockProgrammer;
import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.core.handler.PlayerDataHandler;
import vazkii.psi.common.core.handler.PsiSoundHandler;
import vazkii.psi.common.core.handler.capability.CADData;
import vazkii.psi.common.item.base.IPsiItem;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.item.component.ItemCADSocket;
import vazkii.psi.common.network.message.MessageCADDataSync;
import vazkii.psi.common.network.message.MessageVisualEffect;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemBowCad extends ItemModBow implements ICAD, ISpellSettable, IItemColorProvider, IPsiItem {

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

    public ItemBowCad(String name) {
        super(name);
        setMaxStackSize(1);
        this.addPropertyOverride(new ResourceLocation("pull"), (stack, world, entity) -> entity == null || entity.getActiveItemStack().getItem() != this ? 0 : (stack.getMaxItemUseDuration() - entity.getItemInUseCount()) / 20F);
    }

    private ICADData getCADData(ItemStack stack) {
        if (ICADData.hasData(stack)) return ICADData.data(stack);

        return new CADData();
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        CADData data = new CADData();
        if (nbt != null && nbt.hasKey("Parent", Constants.NBT.TAG_COMPOUND))
            data.deserializeNBT(nbt.getCompoundTag("Parent"));
        return data;
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        CompoundNBT compound = NBTHelper.getOrCreateNBT(stack);

        if (ICADData.hasData(stack)) {
            ICADData data = ICADData.data(stack);

            if (compound.hasKey(TAG_TIME_LEGACY, Constants.NBT.TAG_ANY_NUMERIC)) {
                data.setTime(compound.getInteger(TAG_TIME_LEGACY));
                data.markDirty(true);
                compound.removeTag(TAG_TIME_LEGACY);
            }

            if (compound.hasKey(TAG_STORED_PSI_LEGACY, Constants.NBT.TAG_ANY_NUMERIC)) {
                data.setBattery(compound.getInteger(TAG_STORED_PSI_LEGACY));
                data.markDirty(true);
                compound.removeTag(TAG_STORED_PSI_LEGACY);
            }

            Set<String> keys = new HashSet<>(compound.getKeySet());

            for (String key : keys) {
                Matcher matcher = VECTOR_PREFIX_PATTERN.matcher(key);
                if (matcher.find()) {
                    CompoundNBT vec = compound.getCompoundTag(key);
                    compound.removeTag(key);
                    int memory = Integer.parseInt(matcher.group(1));
                    Vector3 vector = new Vector3(vec.getDouble(TAG_X_LEGACY),
                            vec.getDouble(TAG_Y_LEGACY),
                            vec.getDouble(TAG_Z_LEGACY));
                    data.setSavedVector(memory, vector);
                }
            }

            if (entityIn instanceof ServerPlayerEntity && data.isDirty()) {
                NetworkHandler.INSTANCE.sendTo(new MessageCADDataSync(data), (ServerPlayerEntity) entityIn);
                data.markDirty(false);
            }
        }
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUse(PlayerEntity playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = playerIn.getHeldItem(hand);
        Block block = worldIn.getBlockState(pos).getBlock();
        return block == ModBlocks.programmer ? ((BlockProgrammer) block).setSpell(worldIn, pos, playerIn, stack) : EnumActionResult.PASS;
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, EnumHand handIn)
    {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        PlayerDataHandler.PlayerData data = PlayerDataHandler.get(playerIn);
        boolean flag = !data.overflowed && data.getAvailablePsi() > 0;

        //ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemstack, worldIn, playerIn, handIn, flag);
        //if (ret != null) return ret;
        if (flag) {
            data.deductPsi(100, 20, true, false);
        }
        if (!playerIn.capabilities.isCreativeMode && !flag)
        {
            return flag ? new ActionResult(EnumActionResult.PASS, itemstack) : new ActionResult(EnumActionResult.FAIL, itemstack);
        }
        else
        {
            playerIn.setActiveHand(handIn);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
        }
    }

    @Nonnull
    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft)
    {
        if (entityLiving instanceof PlayerEntity)
        {
            PlayerEntity entityplayer = (PlayerEntity)entityLiving;

            PlayerDataHandler.PlayerData data = PlayerDataHandler.get(entityplayer);

            boolean flag = EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;

            int i = this.getMaxItemUseDuration(stack) - timeLeft;
            i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, entityplayer, i, (!data.overflowed && data.getAvailablePsi() > 0) || flag);
            if (i < 0) return;

            float f = getArrowVelocity(i);

            if ((double)f >= 0.1D)
            {

                if (!worldIn.isRemote)
                {
                    EntityPsiArrow entityarrow = new EntityPsiArrow(worldIn, entityplayer);
                    entityarrow.shoot(entityplayer, entityplayer.rotationPitch, entityplayer.rotationYaw, 0.0F, f * 3.5F, 0.5F);

                    if (f == 1.0F)
                    {
                        entityarrow.setIsCritical(true);
                    }

                    int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);

                    if (j > 0)
                    {
                        entityarrow.setDamage(entityarrow.getDamage() + (double)j * 0.5D + 0.5D);
                    }

                    int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);

                    if (k > 0)
                    {
                        entityarrow.setKnockbackStrength(k);
                    }

                    if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0)
                    {
                        entityarrow.setFire(100);
                    }
                    if (!flag) { //Deduct cost of conjuring arrow
                        data.deductPsi(100, 20, true, false);
                    }
                        //Deduct cost of preventing damage
                    int cost = 150 / (1+EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, stack));
                    data.deductPsi(cost,0,true,false);


                    entityarrow.pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;



                    //Spellcasting Logic here

                    //castSpell(entityplayer, stack, new Vec3d(entityplayer.posX, entityplayer.posY, entityplayer.posZ), entityarrow);

                    worldIn.spawnEntity(entityarrow);
                }

                worldIn.playSound((PlayerEntity)null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

                entityplayer.addStat(Stats.getObjectUseStats(this));

            }
        }
    }

    @Override
    public boolean onEntitySwing (LivingEntity entityLiving, ItemStack itemStackIn) {

        if (entityLiving instanceof PlayerEntity) {
            PlayerEntity playerIn = (PlayerEntity) entityLiving;
            World worldIn = entityLiving.world;
            EnumHand hand = EnumHand.MAIN_HAND;

            PlayerDataHandler.PlayerData data = PlayerDataHandler.get(playerIn);

            ItemStack playerCad = PsiAPI.getPlayerCAD(playerIn);
            if(playerCad != itemStackIn) {
                if(!worldIn.isRemote)
                    playerIn.sendMessage(new TranslationTextComponent("psimisc.multipleCads").setStyle(new Style().setColor(TextFormatting.RED)));
                return false;
            }

            ItemStack bullet = getBulletInSocket(itemStackIn, getSelectedSlot(itemStackIn));
            boolean did = cast(worldIn, playerIn, data, bullet, itemStackIn, 40, 25, 0.5F, ctx -> ctx.castFrom = hand);

            if(!data.overflowed && bullet.isEmpty() && craft(playerIn, "dustRedstone", new ItemStack(ModItems.material))) {
                worldIn.playSound(null, playerIn.posX, playerIn.posY, playerIn.posZ, PsiSoundHandler.cadShoot, SoundCategory.PLAYERS, 0.5F, (float) (0.5 + Math.random() * 0.5));
                data.deductPsi(100, 60, true);

                if(data.level == 0)
                    data.levelUp();
                did = true;
            }
            return did;
        } else {
            return false;
        }
    }
    @Override
    public void setSpell(PlayerEntity player, ItemStack stack, Spell spell) {
        int slot = getSelectedSlot(stack);
        ItemStack bullet = getBulletInSocket(stack, slot);
        if (!bullet.isEmpty() && ISpellAcceptor.isAcceptor(bullet)) {
            ISpellAcceptor.acceptor(bullet).setSpell(player, spell);
            setBulletInSocket(stack, slot, bullet);
            player.getCooldownTracker().setCooldown(stack.getItem(), 10);
        }
    }

    public static boolean cast(World world, PlayerEntity player, PlayerDataHandler.PlayerData data, ItemStack bullet, ItemStack cad, int cd, int particles, float sound, Consumer<SpellContext> predicate) {
        if (!data.overflowed && data.getAvailablePsi() > 0 && !cad.isEmpty() && !bullet.isEmpty() && ISpellAcceptor.hasSpell(bullet) && isTruePlayer(player)) {
            ISpellAcceptor spellContainer = ISpellAcceptor.acceptor(bullet);
            Spell spell = spellContainer.getSpell();
            SpellContext context = new SpellContext().setPlayer(player).setSpell(spell);
            if (predicate != null)
                predicate.accept(context);

            if (context.isValid()) {
                if (context.cspell.metadata.evaluateAgainst(cad)) {
                    int cost = getRealCost(cad, bullet, context.cspell.metadata.stats.get(EnumSpellStat.COST));
                    PreSpellCastEvent event = new PreSpellCastEvent(cost, sound, particles, cd, spell, context, player, data, cad, bullet);
                    if (MinecraftForge.EVENT_BUS.post(event)) {
                        String cancelMessage = event.getCancellationMessage();
                        if (cancelMessage != null && !cancelMessage.isEmpty())
                            player.sendMessage(new TranslationTextComponent(cancelMessage).setStyle(new Style().setColor(TextFormatting.RED)));
                        return false;
                    }

                    cd = event.getCooldown();
                    particles = event.getParticles();
                    sound = event.getSound();
                    cost = event.getCost();

                    spell = event.getSpell();
                    context = event.getContext();

                    if (cost > 0)
                        data.deductPsi(cost, cd, true);

                    if (cost != 0 && sound > 0) {
                        if (!world.isRemote)
                            world.playSound(null, player.posX, player.posY, player.posZ, PsiSoundHandler.cadShoot, SoundCategory.PLAYERS, sound, (float) (0.5 + Math.random() * 0.5));
                        else {
                            int color = Psi.proxy.getColorForCAD(cad);
                            float r = PsiRenderHelper.r(color) / 255F;
                            float g = PsiRenderHelper.g(color) / 255F;
                            float b = PsiRenderHelper.b(color) / 255F;
                            for (int i = 0; i < particles; i++) {
                                double x = player.posX + (Math.random() - 0.5) * 2.1 * player.width;
                                double y = player.posY - player.getYOffset();
                                double z = player.posZ + (Math.random() - 0.5) * 2.1 * player.width;
                                float grav = -0.15F - (float) Math.random() * 0.03F;
                                Psi.proxy.sparkleFX(x, y, z, r, g, b, grav, 0.25F, 15);
                            }

                            double x = player.posX;
                            double y = player.posY + player.getEyeHeight() - 0.1;
                            double z = player.posZ;
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

                    if (!world.isRemote)
                        spellContainer.castSpell(context);
                    MinecraftForge.EVENT_BUS.post(new SpellCastEvent(spell, context, player, data, cad, bullet));
                    return true;
                } else if (!world.isRemote)
                    player.sendMessage(new TranslationTextComponent("psimisc.weakCad").setStyle(new Style().setColor(TextFormatting.RED)));
            }
        }

        return false;
    }

    public static boolean craft(PlayerEntity player, ItemStack in, ItemStack out) {
        return craft(player, CraftingHelper.getIngredient(in), out);
    }

    public static boolean craft(PlayerEntity player, String in, ItemStack out) {
        return craft(player, CraftingHelper.getIngredient(in), out);
    }

    public static boolean craft(PlayerEntity player, Ingredient in, ItemStack out) {
        if (player.world.isRemote)
            return false;

        List<ItemEntity> items = player.getEntityWorld().getEntitiesWithinAABB(ItemEntity.class,
                player.getEntityBoundingBox().grow(8),
                entity -> entity != null && entity.getDistanceSq(player) <= 8 * 8);

        boolean did = false;
        for(ItemEntity item : items) {
            ItemStack stack = item.getItem();
            if(in.test(stack)) {
                ItemStack outCopy = out.copy();
                outCopy.setCount(stack.getCount());
                item.setItem(outCopy);
                did = true;

                NetworkHandler.INSTANCE.sendToAllAround(
                        new MessageVisualEffect(ICADColorizer.DEFAULT_SPELL_COLOR,
                                item.posX, item.posY, item.posZ, item.width, item.height, item.getYOffset(),
                                MessageVisualEffect.TYPE_CRAFT),
                        new NetworkRegistry.TargetPoint(item.world.provider.getDimension(),
                                item.posX, item.posY, item.posZ,
                                32));
            }
        }

        return did;
    }

    public static int getRealCost(ItemStack stack, ItemStack bullet, int cost) {
        if(!stack.isEmpty() && stack.getItem() instanceof ICAD) {
            int eff = ((ICAD) stack.getItem()).getStatValue(stack, EnumCADStat.EFFICIENCY);
            if(eff == -1)
                return -1;
            if(eff == 0)
                return cost;

            double effPercentile = (double) eff / 100;
            double procCost = cost / effPercentile;
            if(!bullet.isEmpty() && ISpellAcceptor.isContainer(bullet))
                procCost *= ISpellAcceptor.acceptor(bullet).getCostModifier();

            return (int) procCost;
        }

        return cost;
    }

    public static boolean isTruePlayer(Entity e) {
        if(!(e instanceof PlayerEntity))
            return false;

        PlayerEntity player = (PlayerEntity) e;

        String name = player.getName();
        return !(player instanceof FakePlayer || FAKE_PLAYER_PATTERN.matcher(name).matches());
    }

    public static void setComponent(ItemStack stack, ItemStack componentStack) {
        if (stack.getItem() instanceof ICAD)
            ((ICAD) stack.getItem()).setCADComponent(stack, componentStack);
    }

    public static ItemStack makeCAD(ItemStack... components) {
        return makeCAD(Arrays.asList(components));
    }

    public static ItemStack makeCADWithAssembly(ItemStack assembly, List<ItemStack> components) {
        ItemStack cad = assembly.getItem() instanceof ICADAssembly ?
                ((ICADAssembly) assembly.getItem()).createCADStack(assembly, components) :
                new ItemStack(Items.bowCAD);

        return makeCAD(cad, components);
    }

    public static ItemStack makeCAD(List<ItemStack> components) {
        return makeCAD(new ItemStack(Items.bowCAD), components);
    }


    public static ItemStack makeCAD(ItemStack base, List<ItemStack> components) {
        ItemStack stack = base.copy();
        for(ItemStack component : components)
            setComponent(stack, component);
        return stack;
    }

    @Override
    public ItemStack getComponentInSlot(ItemStack stack, EnumCADComponent type) {
        String name = TAG_COMPONENT_PREFIX + type.name();
        CompoundNBT cmp = NBTHelper.getCompound(stack, name);

        if(cmp == null)
            return ItemStack.EMPTY;

        return new ItemStack(cmp);
    }

    @Override
    public int getStatValue(ItemStack stack, EnumCADStat stat) {
        int statValue = 0;
        ItemStack componentStack = getComponentInSlot(stack, stat.getSourceType());
        if(!componentStack.isEmpty() && componentStack.getItem() instanceof ICADComponent) {
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
        if(!dye.isEmpty() && dye.getItem() instanceof ICADColorizer)
            return ((ICADColorizer) dye.getItem()).getColor(dye);

        return ICADColorizer.DEFAULT_SPELL_COLOR;
    }

    @Override
    public boolean isSocketSlotAvailable(ItemStack stack, int slot) {
        int sockets = getStatValue(stack, EnumCADStat.SOCKETS);
        if (sockets == -1 || sockets > ItemCADSocket.MAX_SOCKETS)
            sockets = ItemCADSocket.MAX_SOCKETS;
        return slot < sockets;
    }

    @Override
    public ItemStack getBulletInSocket(ItemStack stack, int slot) {
        String name = TAG_BULLET_PREFIX + slot;
        CompoundNBT cmp = NBTHelper.getCompound(stack, name);

        if(cmp == null)
            return ItemStack.EMPTY;

        return new ItemStack(cmp);
    }

    @Override
    public void setBulletInSocket(ItemStack stack, int slot, ItemStack bullet) {
        String name = TAG_BULLET_PREFIX + slot;
        CompoundNBT cmp = new CompoundNBT();

        if(!bullet.isEmpty())
            bullet.writeToNBT(cmp);

        NBTHelper.setCompound(stack, name, cmp);
    }

    @Override
    public int getSelectedSlot(ItemStack stack) {
        return NBTHelper.getInt(stack, TAG_SELECTED_SLOT, 0);
    }

    @Override
    public void setSelectedSlot(ItemStack stack, int slot) {
        NBTHelper.setInt(stack, TAG_SELECTED_SLOT, slot);
    }

    @OnlyIn(Dist.CLIENT)
    public IItemColor getItemColor() {
        return (stack, tintIndex) -> tintIndex == 1 ? getSpellColor(stack) : 0xFFFFFF;
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
        if (maxPsi == -1)
            return;

        int currPsi = getStoredPsi(stack);
        int endPsi = Math.min(currPsi + psi, maxPsi);

        if(endPsi != currPsi) {
            ICADData data = getCADData(stack);
            data.setBattery(endPsi);
            data.markDirty(true);
        }
    }

    @Override
    public int consumePsi(ItemStack stack, int psi) {
        if (psi == 0)
            return 0;

        int currPsi = getStoredPsi(stack);

        if (currPsi == -1)
            return 0;

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
        if (sockets == -1)
            return 0xFF;
        return sockets / 3;
    }

    @Override
    public void setStoredVector(ItemStack stack, int memorySlot, Vector3 vec) throws SpellRuntimeException {
        int size = getMemorySize(stack);
        if(memorySlot < 0 || memorySlot >= size)
            throw new SpellRuntimeException(SpellRuntimeException.MEMORY_OUT_OF_BOUNDS);
        getCADData(stack).setSavedVector(memorySlot, vec);
    }

    @Override
    public Vector3 getStoredVector(ItemStack stack, int memorySlot) throws SpellRuntimeException {
        int size = getMemorySize(stack);
        if(memorySlot < 0 || memorySlot >= size)
            throw new SpellRuntimeException(SpellRuntimeException.MEMORY_OUT_OF_BOUNDS);
        return getCADData(stack).getSavedVector(memorySlot);
    }

    @Override
    public void getSubItems(@Nonnull ItemGroup tab, @Nonnull NonNullList<ItemStack> subItems) {
        if(!isInCreativeTab(tab))
            return;

        // Ivory Psimetal CAD
        subItems.add(makeCAD(new ItemStack(ModItems.cadAssembly, 1, 4),
                new ItemStack(ModItems.cadCore, 1, 3),
                new ItemStack(ModItems.cadSocket, 1, 3),
                new ItemStack(ModItems.cadBattery, 1, 2)));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, ITooltipFlag advanced) {
        TooltipHelper.tooltipIfShift(tooltip, () -> {
            String componentName = local(ISocketable.getSocketedItemName(stack, "psimisc.none"));
            TooltipHelper.addToTooltip(tooltip, "psimisc.spellSelected", componentName);

            for(EnumCADComponent componentType : EnumCADComponent.class.getEnumConstants()) {
                ItemStack componentStack = getComponentInSlot(stack, componentType);
                String name = "psimisc.none";
                if(!componentStack.isEmpty())
                    name = componentStack.getDisplayName();

                name = local(name);
                String line = TextFormatting.GREEN + local(componentType.getName()) + TextFormatting.GRAY + ": " + name;
                TooltipHelper.addToTooltip(tooltip, line);

                for(EnumCADStat stat : EnumCADStat.class.getEnumConstants()) {
                    if(stat.getSourceType() == componentType) {
                        String shrt = stat.getName();
                        int statVal = getStatValue(stack, stat);
                        String statValStr = statVal == -1 ?	"\u221E" : ""+statVal;

                        line = " " + TextFormatting.AQUA + local(shrt) + TextFormatting.GRAY + ": " + statValStr;
                        if(!line.isEmpty())
                            TooltipHelper.addToTooltip(tooltip, line);
                    }
                }
            }
        });
    }

    @Override
    public boolean requiresSneakForSpellSet(ItemStack stack) {
        return true;
    }

    @Nonnull
    @Override
    public Rarity getRarity(ItemStack stack) {
        return Rarity.RARE;
    }

    @OnlyIn(Dist.CLIENT)
    public static String local(String s) {
        return TooltipHandler.local(s);
    }
    @Override
    @OnlyIn(Dist.CLIENT)
    public ItemMeshDefinition getCustomMeshDefinition() {
        return stack -> {
            ICAD cad = (ICAD) stack.getItem();
            ItemStack assemblyStack = cad.getComponentInSlot(stack, EnumCADComponent.ASSEMBLY);
            if(assemblyStack.isEmpty())
                return new ModelResourceLocation("missingno");
            ICADAssembly assembly = (ICADAssembly) assemblyStack.getItem();
            return assembly.getCADModel(assemblyStack, stack);
        };
    }
}
*/