package com.dudblockman.psipherals.items;

import com.dudblockman.psipherals.util.libs.AdvPsimetalToolMaterial;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.Constants;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.*;
import vazkii.psi.api.internal.TooltipHelper;
import vazkii.psi.common.block.BlockProgrammer;
import vazkii.psi.common.block.base.ModBlocks;
import vazkii.psi.common.core.handler.ConfigHandler;
import vazkii.psi.common.core.handler.capability.CADData;
import vazkii.psi.common.item.base.ModItems;
import vazkii.psi.common.network.MessageRegister;
import vazkii.psi.common.network.message.MessageCADDataSync;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class ItemPickaxeCad extends PickaxeItem implements IIntegratedCad {

    public ItemPickaxeCad(Item.Properties props) {
        super(new AdvPsimetalToolMaterial(), 1, -2.8F, props);
    }

    @Override
    public boolean isMelee() {
        return false;
    }

    @Override
    public boolean isTool() {
        return true;
    }

    @Override
    public boolean hitEntity(ItemStack itemstack, LivingEntity target, @Nonnull LivingEntity attacker) {
        super.hitEntity(itemstack, target, attacker);
        castOnAttackEntity(itemstack, target, attacker);
        repairTool(itemstack, attacker);
        return true;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, PlayerEntity player) {
        super.onBlockStartBreak(itemstack, pos, player);
        castOnBlockBreak(itemstack, player);
        return false;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        super.onBlockDestroyed(stack,worldIn,state,pos,entityLiving);
        repairTool(stack, entityLiving);
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
        repairTool(stack, entityIn);
        stack.getCapability(PsiAPI.CAD_DATA_CAPABILITY).ifPresent(data -> {
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
        boolean did = normalCast(worldIn, playerIn, hand);
        return new ActionResult<>(did ? ActionResultType.PASS : ActionResultType.PASS, itemStackIn);
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
        ItemStack cad = assembly.getItem() instanceof ICADAssembly ? ((ICADAssembly) assembly.getItem()).createCADStack(assembly, components) : new ItemStack(Items.pickaxeCAD);

        return makeCAD(cad, components);
    }

    public static ItemStack makeCAD(List<ItemStack> components) {
        return makeCAD(new ItemStack(Items.pickaxeCAD), components);
    }

    public static ItemStack makeCAD(ItemStack base, List<ItemStack> components) {
        ItemStack stack = base.copy();
        for (ItemStack component : components) {
            setComponent(stack, component);
        }
        return stack;
    }

    @Override
    public int getHarvestLevel(ItemStack stack, ToolType tool, @Nullable PlayerEntity player, @Nullable BlockState blockState) {
        int level = super.getHarvestLevel(stack, tool, player, blockState);
        return level < 0 ? -1 : Math.max(level, ConfigHandler.COMMON.cadHarvestLevel.get());
    }

    @Override
    public void fillItemGroup(@Nonnull ItemGroup tab, @Nonnull NonNullList<ItemStack> subItems) {
        if (!isInGroup(tab)) {
            return;
        }

        // Psimetal CAD
        subItems.add(makeCAD(new ItemStack(Items.pickaxeAssemblyPsimetal),
                new ItemStack(ModItems.cadCoreOverclocked),
                new ItemStack(ModItems.cadSocketSignaling),
                new ItemStack(ModItems.cadBatteryExtended)));
        // Ebony Psimetal CAD
        subItems.add(makeCAD(new ItemStack(Items.pickaxeAssemblyEbonyPsimetal),
                new ItemStack(ModItems.cadCoreHyperClocked),
                new ItemStack(ModItems.cadSocketTransmissive),
                new ItemStack(ModItems.cadBatteryUltradense)));
        // Ivory Psimetal CAD
        subItems.add(makeCAD(new ItemStack(Items.pickaxeAssemblyIvoryPsimetal),
                new ItemStack(ModItems.cadCoreHyperClocked),
                new ItemStack(ModItems.cadSocketTransmissive),
                new ItemStack(ModItems.cadBatteryUltradense)));
        // Creative CAD
        subItems.add(makeCAD(new ItemStack(Items.pickaxeAssemblyCreative),
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
