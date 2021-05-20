package com.dudblockman.psipherals.crafting;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class PsilonInfusionRecipe implements IRecipe<RecipeWrapper> {
    public static final IRecipeSerializer<PsilonInfusionRecipe> SERIALIZER = new Serializer();

    private final Ingredient focus;
    private final List<Ingredient> ingredients;
    private final ItemStack output;
    private final ResourceLocation id;

    public PsilonInfusionRecipe(ResourceLocation id, Ingredient focus, List<Ingredient> ingredients, ItemStack output) {
        this.id = id;
        this.focus = focus;
        this.ingredients = ingredients;
        this.output = output;
    }

    @Override
    public boolean matches(RecipeWrapper inv, World worldIn) {
        if (focus.test(inv.getStackInSlot(0))) {
            java.util.List<ItemStack> inputs = new java.util.ArrayList<>();
            int i = 0;

            for(int j = 1; j < inv.getSizeInventory(); ++j) {
                ItemStack itemstack = inv.getStackInSlot(j);
                if (!itemstack.isEmpty()) {
                    ++i;
                    inputs.add(itemstack);
                }
            }

            return i == this.ingredients.size() && net.minecraftforge.common.util.RecipeMatcher.findMatches(inputs,  this.ingredients) != null;
        }
        return false;
    }

    @Override
    public ItemStack getCraftingResult(RecipeWrapper inv) {
        return getRecipeOutput();
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return output;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public IRecipeType<?> getType() {
        return CraftingRecipes.INFUSION_TYPE;
    }

    static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<PsilonInfusionRecipe> {
        @Override
        public PsilonInfusionRecipe read(ResourceLocation id, JsonObject json) {
            Ingredient focus = Ingredient.deserialize(JSONUtils.getJsonObject(json, "focus"));

            List<Ingredient> ingredients = new ArrayList<>();
            JsonArray a = JSONUtils.getJsonArray(json, "ingredients");
            for (JsonElement b : a) {
                ingredients.add(Ingredient.deserialize(b));
            }

            ItemStack output = CraftingHelper.getItemStack(JSONUtils.getJsonObject(json, "output"), true);

            return new PsilonInfusionRecipe(id, focus, ingredients, output);
        }

        @Nullable
        @Override
        public PsilonInfusionRecipe read(ResourceLocation id, PacketBuffer buf) {
            Ingredient focus = Ingredient.read(buf);

            int length = buf.readInt();
            List<Ingredient> ingredients = new ArrayList<>();
            for (int i = 0; i < length; ++i) {
                ingredients.add(Ingredient.read(buf));
            }

            ItemStack output = buf.readItemStack();
            return new PsilonInfusionRecipe(id, focus, ingredients, output);
        }

        @Override
        public void write(PacketBuffer buf, PsilonInfusionRecipe recipe) {
            recipe.focus.write(buf);

            buf.writeInt(recipe.ingredients.size());
            for (Ingredient ingredient : recipe.ingredients) {
                ingredient.write(buf);
            }

            buf.writeItemStack(recipe.output);
        }
    }
}
