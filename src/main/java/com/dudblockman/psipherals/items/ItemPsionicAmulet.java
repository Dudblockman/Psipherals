package com.dudblockman.psipherals.items;

import com.dudblockman.psipherals.capability.AmuletCapabilityProvider;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import vazkii.psi.api.cad.ISocketable;
import vazkii.psi.api.internal.TooltipHelper;

import javax.annotation.Nullable;
import java.util.List;

public class ItemPsionicAmulet extends Item {

    public ItemPsionicAmulet(Item.Properties props) {
        super(props.maxStackSize(1).defaultMaxDamage(0));
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, CompoundNBT unused) {
        return new AmuletCapabilityProvider(stack,1);
    }
    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World playerin, List<ITextComponent> tooltip, ITooltipFlag advanced) {
        TooltipHelper.tooltipIfShift(tooltip, () -> {
            String key = "psimisc.none";
            if (stack.getTag() != null && stack.getTag().contains("spell") && stack.getTag().getCompound("spell").contains("spellName")) {
                key = stack.getTag().getCompound("spell").getString("spellName");
            }
            tooltip.add(new TranslationTextComponent("psipherals.primary_spell_selected", key));
            ITextComponent componentName = ISocketable.getSocketedItemName(stack, "psimisc.none");
            tooltip.add(new TranslationTextComponent("psipherals.secondary_spell_selected", componentName));
        });
    }
}

