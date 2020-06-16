package com.dudblockman.psipherals.items;

import com.teamwizardry.librarianlib.features.base.item.ItemMod;
import com.teamwizardry.librarianlib.features.utilities.client.TooltipHelper;
import org.apache.commons.lang3.tuple.Pair;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.api.cad.ICADComponent;

import java.util.HashMap;
import java.util.List;

public abstract class ItemAdvComponent extends ItemMod implements ICADComponent{
    private final HashMap<Pair<EnumCADStat, Integer>, Integer> stats = new HashMap<>();

    public ItemAdvComponent(String name, String... variants) {
        super(name, variants);
        setMaxStackSize(1);
        registerStats();
    }

    public void registerStats() {
        // NO-OP
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, World playerIn, List<String> tooltip, ITooltipFlag advanced) {
        TooltipHelper.tooltipIfShift(tooltip, () -> {
            EnumCADComponent componentType = getComponentType(stack);

            String componentName = TooltipHelper.local(componentType.getName());
            TooltipHelper.addToTooltip(tooltip, "psimisc.componentType", componentName);
            for(EnumCADStat stat : EnumCADStat.class.getEnumConstants()) {
                if(stat.getSourceType() == componentType) {
                    int statVal = getCADStatValue(stack, stat);
                    String statValStr = statVal == -1 ?	"\u221E" : ""+statVal;

                    String name = TooltipHelper.local(stat.getName());
                    tooltip.add(" " + TextFormatting.AQUA + name + TextFormatting.GRAY + ": " + statValStr);
                }
            }
        });
    }

    public void addStat(EnumCADStat stat, int meta, int value) {
        stats.put(Pair.of(stat, meta), value);
    }

    @Override
    public int getCADStatValue(ItemStack stack, EnumCADStat stat) {
        Pair p = Pair.of(stat, stack.getItemDamage());
        if(stats.containsKey(p))
            return stats.get(p);

        return 0;
    }
}
