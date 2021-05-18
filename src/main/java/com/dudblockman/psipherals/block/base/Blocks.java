package com.dudblockman.psipherals.block.base;

import com.dudblockman.psipherals.Psipherals;
import com.dudblockman.psipherals.block.BlockPsilon;
import com.dudblockman.psipherals.util.libs.BlockNames;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

import static com.dudblockman.psipherals.items.Items.defaultBuilder;

@Mod.EventBusSubscriber(modid = Psipherals.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Blocks {
    public static final Block psilon = new BlockPsilon(Block.Properties.create(Material.IRON).hardnessAndResistance(5, 10).sound(SoundType.METAL).notSolid());
    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> evt) {
        IForgeRegistry<Block> r = evt.getRegistry();
        r.register(psilon.setRegistryName(Psipherals.location(BlockNames.PSILON)));

    }
    @SubscribeEvent
    public static void registerItemBlocks(RegistryEvent.Register<Item> evt) {
        IForgeRegistry<Item> r = evt.getRegistry();
        r.register(new BlockItem(psilon, defaultBuilder().rarity(Rarity.UNCOMMON)).setRegistryName(psilon.getRegistryName()));

    }
}
