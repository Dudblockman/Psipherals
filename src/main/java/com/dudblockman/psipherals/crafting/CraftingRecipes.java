package com.dudblockman.psipherals.crafting;

import com.dudblockman.psipherals.Psipherals;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistryEntry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Psipherals.MODID)
public class CraftingRecipes {
    public static final IRecipeType<PsilonInfusionRecipe> INFUSION_TYPE = new CraftingRecipes.RecipeType();

    @SubscribeEvent
    public static void registerSerializers(RegistryEvent.Register<IRecipeSerializer<?>> event) {
        event.getRegistry().registerAll(
                name(PsilonInfusionRecipe.SERIALIZER, "infusion")
        );

    }

    private static <T extends IForgeRegistryEntry<? extends T>> T name(T entry, String name) {
        return entry.setRegistryName(Psipherals.location(name));
    }

    private static class RecipeType<T extends IRecipe<?>> implements IRecipeType<T> {
        @Override
        public String toString() {
            return Registry.RECIPE_TYPE.getKey(this).toString();
        }
    }
}
