package com.dudblockman.psipherals.items;

import com.dudblockman.psipherals.Psipherals;
import com.dudblockman.psipherals.util.CreativeTab;
import com.dudblockman.psipherals.util.libs.ItemNames;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = Psipherals.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Items {
    public static final ItemPsimetalBow psimetalBow = new ItemPsimetalBow(defaultBuilder(), ItemNames.PSIMETAL_BOW);

    public static final Item swordCAD = new ItemSwordCad(defaultBuilder());

    public static final Item swordAssemblyPsimetal = new ItemSwordAssembly(defaultBuilder(), ItemNames.SWORD_CAD_PSIMETAL);
    public static final Item swordAssemblyIvoryPsimetal = new ItemSwordAssembly(defaultBuilder(), ItemNames.SWORD_CAD_IVORY_PSIMETAL);
    public static final Item swordAssemblyEbonyPsimetal = new ItemSwordAssembly(defaultBuilder(), ItemNames.SWORD_CAD_EBONY_PSIMETAL);
    public static final Item swordAssemblyCreative = new ItemSwordAssembly(defaultBuilder(), ItemNames.SWORD_CAD_CREATIVE);

    public static final Item pickaxeCAD = new ItemPickaxeCad(defaultBuilder());

    public static final Item pickaxeAssemblyPsimetal = new ItemPickaxeAssembly(defaultBuilder(), ItemNames.PICKAXE_CAD_PSIMETAL);
    public static final Item pickaxeAssemblyIvoryPsimetal = new ItemPickaxeAssembly(defaultBuilder(), ItemNames.PICKAXE_CAD_IVORY_PSIMETAL);
    public static final Item pickaxeAssemblyEbonyPsimetal = new ItemPickaxeAssembly(defaultBuilder(), ItemNames.PICKAXE_CAD_EBONY_PSIMETAL);
    public static final Item pickaxeAssemblyCreative = new ItemPickaxeAssembly(defaultBuilder(), ItemNames.PICKAXE_CAD_CREATIVE);


    public static final Item shovelCAD = new ItemShovelCad(defaultBuilder());

    public static final Item shovelAssemblyPsimetal = new ItemShovelAssembly(defaultBuilder(), ItemNames.SHOVEL_CAD_PSIMETAL);
    public static final Item shovelAssemblyIvoryPsimetal = new ItemShovelAssembly(defaultBuilder(), ItemNames.SHOVEL_CAD_IVORY_PSIMETAL);
    public static final Item shovelAssemblyEbonyPsimetal = new ItemShovelAssembly(defaultBuilder(), ItemNames.SHOVEL_CAD_EBONY_PSIMETAL);
    public static final Item shovelAssemblyCreative = new ItemShovelAssembly(defaultBuilder(), ItemNames.SHOVEL_CAD_CREATIVE);


    public static final Item axeCAD = new ItemAxeCad(defaultBuilder());

    public static final Item axeAssemblyPsimetal = new ItemAxeAssembly(defaultBuilder(), ItemNames.AXE_CAD_PSIMETAL);
    public static final Item axeAssemblyIvoryPsimetal = new ItemAxeAssembly(defaultBuilder(), ItemNames.AXE_CAD_IVORY_PSIMETAL);
    public static final Item axeAssemblyEbonyPsimetal = new ItemAxeAssembly(defaultBuilder(), ItemNames.AXE_CAD_EBONY_PSIMETAL);
    public static final Item axeAssemblyCreative = new ItemAxeAssembly(defaultBuilder(), ItemNames.AXE_CAD_CREATIVE);

    public static final Item psiAmulet = new ItemPsionicAmulet(defaultBuilder());

    public static final Item dessAssembly = new ItemSwordAssembly(new Item.Properties(),"igalima");

    public static final ItemBowCad bowCAD = new ItemBowCad(defaultBuilder());
    public static Item.Properties defaultBuilder() {
        return new Item.Properties().group(CreativeTab.INSTANCE);
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> evt) {

        IForgeRegistry<Item> r = evt.getRegistry();
        r.register(psimetalBow.setRegistryName(Psipherals.MODID, ItemNames.PSIMETAL_BOW));

        r.register(bowCAD.setRegistryName(Psipherals.MODID, ItemNames.PSIMETAL_BOW_ADVANCED));

        r.register(swordCAD.setRegistryName(Psipherals.MODID, ItemNames.PSIMETAL_SWORD_ADVANCED));

        r.register(swordAssemblyPsimetal.setRegistryName(Psipherals.MODID, ItemNames.SWORD_ASSEMBLY_PSIMETAL));
        r.register(swordAssemblyIvoryPsimetal.setRegistryName(Psipherals.MODID, ItemNames.SWORD_ASSEMBLY_IVORY_PSIMETAL));
        r.register(swordAssemblyEbonyPsimetal.setRegistryName(Psipherals.MODID, ItemNames.SWORD_ASSEMBLY_EBONY_PSIMETAL));
        r.register(swordAssemblyCreative.setRegistryName(Psipherals.MODID, ItemNames.SWORD_ASSEMBLY_CREATIVE));


        r.register(pickaxeCAD.setRegistryName(Psipherals.MODID, ItemNames.PSIMETAL_PICKAXE_ADVANCED));

        r.register(pickaxeAssemblyPsimetal.setRegistryName(Psipherals.MODID, ItemNames.PICKAXE_ASSEMBLY_PSIMETAL));
        r.register(pickaxeAssemblyIvoryPsimetal.setRegistryName(Psipherals.MODID, ItemNames.PICKAXE_ASSEMBLY_IVORY_PSIMETAL));
        r.register(pickaxeAssemblyEbonyPsimetal.setRegistryName(Psipherals.MODID, ItemNames.PICKAXE_ASSEMBLY_EBONY_PSIMETAL));
        r.register(pickaxeAssemblyCreative.setRegistryName(Psipherals.MODID, ItemNames.PICKAXE_ASSEMBLY_CREATIVE));


        r.register(shovelCAD.setRegistryName(Psipherals.MODID, ItemNames.PSIMETAL_SHOVEL_ADVANCED));

        r.register(shovelAssemblyPsimetal.setRegistryName(Psipherals.MODID, ItemNames.SHOVEL_ASSEMBLY_PSIMETAL));
        r.register(shovelAssemblyIvoryPsimetal.setRegistryName(Psipherals.MODID, ItemNames.SHOVEL_ASSEMBLY_IVORY_PSIMETAL));
        r.register(shovelAssemblyEbonyPsimetal.setRegistryName(Psipherals.MODID, ItemNames.SHOVEL_ASSEMBLY_EBONY_PSIMETAL));
        r.register(shovelAssemblyCreative.setRegistryName(Psipherals.MODID, ItemNames.SHOVEL_ASSEMBLY_CREATIVE));


        r.register(axeCAD.setRegistryName(Psipherals.MODID, ItemNames.PSIMETAL_AXE_ADVANCED));

        r.register(axeAssemblyPsimetal.setRegistryName(Psipherals.MODID, ItemNames.AXE_ASSEMBLY_PSIMETAL));
        r.register(axeAssemblyIvoryPsimetal.setRegistryName(Psipherals.MODID, ItemNames.AXE_ASSEMBLY_IVORY_PSIMETAL));
        r.register(axeAssemblyEbonyPsimetal.setRegistryName(Psipherals.MODID, ItemNames.AXE_ASSEMBLY_EBONY_PSIMETAL));
        r.register(axeAssemblyCreative.setRegistryName(Psipherals.MODID, ItemNames.AXE_ASSEMBLY_CREATIVE));

        r.register(psiAmulet.setRegistryName(Psipherals.MODID, ItemNames.PSIONIC_AMULET));

        r.register(dessAssembly.setRegistryName(Psipherals.MODID, "igalima"));
    }
}
