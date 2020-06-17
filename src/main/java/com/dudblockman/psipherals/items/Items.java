package com.dudblockman.psipherals.items;

import com.dudblockman.psipherals.Psipherals;
import com.dudblockman.psipherals.util.CreativeTab;
import com.dudblockman.psipherals.util.libs.ItemNames;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import vazkii.psi.common.core.handler.capability.CapabilityHandler;

@Mod.EventBusSubscriber(modid = Psipherals.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Items {
    public static final ItemPsimetalBow psimetalBow = new ItemPsimetalBow(ItemNames.PSIMETAL_BOW);

    //public static final Item swordCAD = new ItemSwordCad(ItemNames.PSIMETAL_SWORD_ADVANCED);
    //public static final Item swordAssembly = new ItemSwordAssembly(ItemNames.SWORD_ASSEMBLY);

    //public static final Item pickaxeCAD = new ItemPickaxeCad(ItemNames.PSIMETAL_PICKAXE_ADVANCED);
    //public static final Item pickaxeAssembly = new ItemPickaxeAssembly(ItemNames.PICKAXE_ASSEMBLY);

    //public static final Item shovelCAD = new ItemShovelCad(ItemNames.PSIMETAL_SHOVEL_ADVANCED);
    //public static final Item shovelAssembly = new ItemShovelAssembly(ItemNames.SHOVEL_ASSEMBLY);

    public static final Item axeCAD = new ItemAxeCad(defaultBuilder(), ItemNames.PSIMETAL_AXE_ADVANCED);
    public static final Item axeAssembly = new ItemAxeAssembly(defaultBuilder(), ItemNames.AXE_ASSEMBLY);

    //public static final ItemBowCad bowCAD = new ItemBowCad(ItemNames.PSIMETAL_BOW_ADVANCED);
    public static Item.Properties defaultBuilder() {
        return new Item.Properties().group(CreativeTab.INSTANCE);
    }
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> evt) {
        CapabilityHandler.register();

        IForgeRegistry<Item> r = evt.getRegistry();
        r.register(axeCAD.setRegistryName(Psipherals.MODID, ItemNames.PSIMETAL_AXE_ADVANCED));
        r.register(axeAssembly.setRegistryName(Psipherals.MODID, ItemNames.AXE_ASSEMBLY));
    }
}
