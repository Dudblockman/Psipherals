package com.dudblockman.psipherals.items;

import com.dudblockman.psipherals.Psipherals;
import com.dudblockman.psipherals.util.libs.ItemNames;
import com.teamwizardry.librarianlib.features.base.item.*;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Psipherals.MODID)
public class Items {
    public static final ItemModBow psimetalBow = new ItemPsimetalBow(ItemNames.PSIMETAL_BOW);
    public static final ItemSwordCad swordCAD = new ItemSwordCad(ItemNames.PSIMETAL_SWORD_ADVANCED);
    public static final ItemBowCad bowCAD = new ItemBowCad(ItemNames.PSIMETAL_BOW_ADVANCED);
}
