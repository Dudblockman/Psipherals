package com.dudblockman.psipherals.items;

import com.dudblockman.psipherals.Psipherals;
import com.dudblockman.psipherals.spell.base.SpellPieces;
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

    public static final Item swordCAD = new ItemSwordCad(fireproofBuilder());

    public static final Item swordAssemblyPsimetal = new ItemSwordAssembly(fireproofBuilder(), ItemNames.SWORD_CAD_PSIMETAL);
    public static final Item swordAssemblyIvoryPsimetal = new ItemSwordAssembly(fireproofBuilder(), ItemNames.SWORD_CAD_IVORY_PSIMETAL);
    public static final Item swordAssemblyEbonyPsimetal = new ItemSwordAssembly(fireproofBuilder(), ItemNames.SWORD_CAD_EBONY_PSIMETAL);
    public static final Item swordAssemblyCreative = new ItemSwordAssembly(fireproofBuilder(), ItemNames.SWORD_CAD_CREATIVE);

    public static final Item pickaxeCAD = new ItemPickaxeCad(fireproofBuilder());

    public static final Item pickaxeAssemblyPsimetal = new ItemPickaxeAssembly(fireproofBuilder(), ItemNames.PICKAXE_CAD_PSIMETAL);
    public static final Item pickaxeAssemblyIvoryPsimetal = new ItemPickaxeAssembly(fireproofBuilder(), ItemNames.PICKAXE_CAD_IVORY_PSIMETAL);
    public static final Item pickaxeAssemblyEbonyPsimetal = new ItemPickaxeAssembly(fireproofBuilder(), ItemNames.PICKAXE_CAD_EBONY_PSIMETAL);
    public static final Item pickaxeAssemblyCreative = new ItemPickaxeAssembly(fireproofBuilder(), ItemNames.PICKAXE_CAD_CREATIVE);

    public static final Item axeCAD = new ItemAxeCad(fireproofBuilder());

    public static final Item axeAssemblyPsimetal = new ItemAxeAssembly(fireproofBuilder(), ItemNames.AXE_CAD_PSIMETAL);
    public static final Item axeAssemblyIvoryPsimetal = new ItemAxeAssembly(fireproofBuilder(), ItemNames.AXE_CAD_IVORY_PSIMETAL);
    public static final Item axeAssemblyEbonyPsimetal = new ItemAxeAssembly(fireproofBuilder(), ItemNames.AXE_CAD_EBONY_PSIMETAL);
    public static final Item axeAssemblyCreative = new ItemAxeAssembly(fireproofBuilder(), ItemNames.AXE_CAD_CREATIVE);

    public static final Item psiAmulet = new ItemPsionicAmulet(defaultBuilder());

    public static final Item dessAssembly = new ItemSwordAssembly(new Item.Properties(),"igalima");

    public static final Item pentaGrenadeSpellBullet = new ItemPentaGrenadeSpellBullet(defaultBuilder());

    public static final ItemBowCad bowCAD = new ItemBowCad(defaultBuilder());
    public static Item.Properties defaultBuilder() {
        return new Item.Properties().group(CreativeTab.INSTANCE);
    }
    public static Item.Properties fireproofBuilder() { return defaultBuilder().isImmuneToFire(); }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> evt) {

        SpellPieces.init();

        IForgeRegistry<Item> r = evt.getRegistry();
        r.register(psimetalBow.setRegistryName(Psipherals.location(ItemNames.PSIMETAL_BOW)));

        r.register(bowCAD.setRegistryName(Psipherals.location(ItemNames.PSIMETAL_BOW_ADVANCED)));

        r.register(swordCAD.setRegistryName(Psipherals.location(ItemNames.PSIMETAL_SWORD_ADVANCED)));

        r.register(swordAssemblyPsimetal.setRegistryName(Psipherals.location(ItemNames.SWORD_ASSEMBLY_PSIMETAL)));
        r.register(swordAssemblyIvoryPsimetal.setRegistryName(Psipherals.location(ItemNames.SWORD_ASSEMBLY_IVORY_PSIMETAL)));
        r.register(swordAssemblyEbonyPsimetal.setRegistryName(Psipherals.location(ItemNames.SWORD_ASSEMBLY_EBONY_PSIMETAL)));
        r.register(swordAssemblyCreative.setRegistryName(Psipherals.location(ItemNames.SWORD_ASSEMBLY_CREATIVE)));


        r.register(pickaxeCAD.setRegistryName(Psipherals.location(ItemNames.PSIMETAL_PICKAXE_ADVANCED)));

        r.register(pickaxeAssemblyPsimetal.setRegistryName(Psipherals.location(ItemNames.PICKAXE_ASSEMBLY_PSIMETAL)));
        r.register(pickaxeAssemblyIvoryPsimetal.setRegistryName(Psipherals.location(ItemNames.PICKAXE_ASSEMBLY_IVORY_PSIMETAL)));
        r.register(pickaxeAssemblyEbonyPsimetal.setRegistryName(Psipherals.location(ItemNames.PICKAXE_ASSEMBLY_EBONY_PSIMETAL)));
        r.register(pickaxeAssemblyCreative.setRegistryName(Psipherals.location(ItemNames.PICKAXE_ASSEMBLY_CREATIVE)));


        r.register(axeCAD.setRegistryName(Psipherals.location(ItemNames.PSIMETAL_AXE_ADVANCED)));

        r.register(axeAssemblyPsimetal.setRegistryName(Psipherals.location(ItemNames.AXE_ASSEMBLY_PSIMETAL)));
        r.register(axeAssemblyIvoryPsimetal.setRegistryName(Psipherals.location(ItemNames.AXE_ASSEMBLY_IVORY_PSIMETAL)));
        r.register(axeAssemblyEbonyPsimetal.setRegistryName(Psipherals.location(ItemNames.AXE_ASSEMBLY_EBONY_PSIMETAL)));
        r.register(axeAssemblyCreative.setRegistryName(Psipherals.location(ItemNames.AXE_ASSEMBLY_CREATIVE)));

        r.register(psiAmulet.setRegistryName(Psipherals.location(ItemNames.PSIONIC_AMULET)));

        r.register(dessAssembly.setRegistryName(Psipherals.location("igalima")));

        r.register(pentaGrenadeSpellBullet.setRegistryName(Psipherals.location(ItemNames.PENTA_GRENADE)));
    }
}
