package com.dudblockman.psipherals.jei;

import com.dudblockman.psipherals.Psipherals;
import com.dudblockman.psipherals.block.base.Blocks;
import com.dudblockman.psipherals.crafting.PsilonInfusionRecipe;
import com.dudblockman.psipherals.spell.base.SpellPieces;
import com.dudblockman.psipherals.spell.trick.TrickPsilon;
import com.dudblockman.psipherals.util.libs.PieceNames;
import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.text.ITextComponent;
import vazkii.psi.api.ClientPsiAPI;
import vazkii.psi.api.recipe.ITrickRecipe;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.client.jei.tricks.DrawableTAS;
import vazkii.psi.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PsilonInfusionCategory implements IRecipeCategory<PsilonInfusionRecipe> {
    public static final ResourceLocation UID = Psipherals.location("psilon_crafting");

    private final IGuiHelper helper;

    private static final int trickX = 91;
    private static final int trickY = 58;

    private final IDrawable background;
    private final IDrawable overlay;
    private final IDrawable trick;
    private final IDrawable icon;

    public PsilonInfusionCategory(IGuiHelper helper) {
        this.helper = helper;
        background = helper.createDrawable(Psipherals.location("textures/gui/infusion.png"), 0, 0, 155, 100);
        overlay = helper.createDrawable(new ResourceLocation(LibMisc.MOD_ID, "textures/gui/jei/trick.png"), 0, 0, 96, 41);
        trick = new DrawableTAS(ClientPsiAPI.getSpellPieceMaterial(Psipherals.location(PieceNames.PSILON)).getSprite());
        icon = helper.createDrawableIngredient(new ItemStack(Blocks.psilon.asItem()));
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends PsilonInfusionRecipe> getRecipeClass() {
        return PsilonInfusionRecipe.class;
    }

    @Override
    public String getTitle() {
        return I18n.format("jei.psipherals.category.psilon_crafting");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setIngredients(PsilonInfusionRecipe recipe, IIngredients ingredients) {
        ingredients.setInputLists(VanillaTypes.ITEM, recipe.getRecipeInput());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
    }

    @Override
    public void draw(PsilonInfusionRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        trick.draw(matrixStack,trickX,trickY);
    }

    private static boolean onTrick(double mouseX, double mouseY) {
        return (mouseX >= trickX && mouseX <= trickX + 16 && mouseY >= trickY && mouseY <= trickY + 16);
    }

    @Nonnull
    @Override
    public List<ITextComponent> getTooltipStrings(PsilonInfusionRecipe recipe, double mouseX, double mouseY) {
        if (onTrick(mouseX, mouseY)) {
            List<ITextComponent> tooltip = new ArrayList<>();
            new TrickPsilon(new Spell()).getTooltip(tooltip);
            return tooltip;
        }
        return Collections.emptyList();
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, PsilonInfusionRecipe psilonInfusionRecipe, IIngredients ingredients) {
        Vector2f point = new Vector2f(41, 10);
        Vector2f center = new Vector2f(41, 41);
        recipeLayout.getItemStacks().init(0, true, (int) center.x, (int) center.y);
        recipeLayout.getItemStacks().set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));
        /*if (ingredients.getInputs(VanillaTypes.ITEM).size() > 12) {
            point = new Vector2f(41, 5);
        }*/

        double angleBetweenEach = 360.0 / (ingredients.getInputs(VanillaTypes.ITEM).size()-1);
        int index = 1;
        for (; index < ingredients.getInputs(VanillaTypes.ITEM).size(); index++) {
            Vector2f pos = rotatePointAbout(point, center, angleBetweenEach * (index-1));
            recipeLayout.getItemStacks().init(index, true, (int) pos.x, (int) pos.y);
            recipeLayout.getItemStacks().set(index, ingredients.getInputs(VanillaTypes.ITEM).get(index));
        }

        recipeLayout.getItemStacks().init(index, false, 117, 40);
        recipeLayout.getItemStacks().set(index, ingredients.getOutputs(VanillaTypes.ITEM).get(0));

    }

    public static Vector2f rotatePointAbout(Vector2f in, Vector2f about, double degrees) {
        double rad = degrees * Math.PI / 180.0;
        double newX = Math.cos(rad) * (in.x - about.x) - Math.sin(rad) * (in.y - about.y) + about.x;
        double newY = Math.sin(rad) * (in.x - about.x) + Math.cos(rad) * (in.y - about.y) + about.y;
        return new Vector2f((float) newX, (float) newY);
    }
}
