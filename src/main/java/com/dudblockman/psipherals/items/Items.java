package com.dudblockman.psipherals.items;

import com.dudblockman.psipherals.Psipherals;
import com.dudblockman.psipherals.util.libs.ItemNames;
import com.teamwizardry.librarianlib.features.base.item.*;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Psipherals.MODID)
public class Items {
    public static final ItemModBow psimetalBow = new ItemPsimetalBow(ItemNames.PSIMETAL_BOW);

    public static final ItemSwordCad swordCAD = new ItemSwordCad(ItemNames.PSIMETAL_SWORD_ADVANCED);
    public static final ItemSwordAssembly swordAssembly = new ItemSwordAssembly(ItemNames.SWORD_ASSEMBLY);

    public static final ItemPickaxeCad pickaxeCAD = new ItemPickaxeCad(ItemNames.PSIMETAL_PICKAXE_ADVANCED);
    public static final ItemPickaxeAssembly pickaxeAssembly = new ItemPickaxeAssembly(ItemNames.PICKAXE_ASSEMBLY);

    public static final ItemShovelCad shovelCAD = new ItemShovelCad(ItemNames.PSIMETAL_SHOVEL_ADVANCED);
    public static final ItemShovelAssembly shovelAssembly = new ItemShovelAssembly(ItemNames.SHOVEL_ASSEMBLY);

    public static final ItemAxeCad axeCAD = new ItemAxeCad(ItemNames.PSIMETAL_AXE_ADVANCED);
    public static final ItemAxeAssembly axeAssembly = new ItemAxeAssembly(ItemNames.AXE_ASSEMBLY);

    public static final ItemBowCad bowCAD = new ItemBowCad(ItemNames.PSIMETAL_BOW_ADVANCED);
}
