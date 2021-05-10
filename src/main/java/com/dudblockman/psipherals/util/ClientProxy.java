package com.dudblockman.psipherals.util;

import com.dudblockman.psipherals.Psipherals;
import com.dudblockman.psipherals.entity.Entities;
import com.dudblockman.psipherals.entity.RenderPsiArrow;
import com.dudblockman.psipherals.items.ItemAxeCad;
import com.dudblockman.psipherals.items.ItemPickaxeCad;
import com.dudblockman.psipherals.items.ItemSwordCad;
import com.dudblockman.psipherals.items.Items;
import com.dudblockman.psipherals.util.libs.ItemNames;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import vazkii.psi.client.model.ModelCAD;

import java.util.Objects;

@OnlyIn(Dist.CLIENT)
public class ClientProxy implements IProxy {

    public void registerHandlers() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::modelBake);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::addCADModels);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::loadComplete);

    }

    private void clientSetup(FMLClientSetupEvent event) {
        RenderingRegistry.registerEntityRenderingHandler(Entities.arrowEntityType, RenderPsiArrow::new);
        //ModelBakery.LOCATIONS_BUILTIN_TEXTURES.addAll(PsiAPI.getAllSpellPieceMaterial());
        DeferredWorkQueue.runLater(() -> {
            registerPropertyGetters();
        });
    }
    private void modelBake(ModelBakeEvent event) {
        Item[] cads = {Items.swordCAD,Items.pickaxeCAD,Items.axeCAD};
        for (Item cad : cads) {
            ModelResourceLocation key = new ModelResourceLocation(Objects.requireNonNull(cad.getRegistryName()), "inventory");
            event.getModelRegistry().put(key, new ModelCAD());
        }
    }
    private static void registerPropertyGetter(IItemProvider item, ResourceLocation id, IItemPropertyGetter propGetter) {
        ItemModelsProperties.registerProperty(item.asItem(), id, propGetter);
    }

    private static void registerPropertyGetters() {
        IItemPropertyGetter pulling = ItemModelsProperties.func_239417_a_(net.minecraft.item.Items.BOW, new ResourceLocation("pulling"));
        IItemPropertyGetter pull = ItemModelsProperties.func_239417_a_(net.minecraft.item.Items.BOW, new ResourceLocation("pull"));
        registerPropertyGetter(Items.psimetalBow, new ResourceLocation("pulling"), pulling);
        registerPropertyGetter(Items.psimetalBow, new ResourceLocation("pull"), pull);
        registerPropertyGetter(Items.bowCAD, new ResourceLocation("pulling"), pulling);
        registerPropertyGetter(Items.bowCAD, new ResourceLocation("pull"), pull);

        IItemPropertyGetter cpulling = ItemModelsProperties.func_239417_a_(net.minecraft.item.Items.CROSSBOW, new ResourceLocation("pulling"));
        IItemPropertyGetter cpull = ItemModelsProperties.func_239417_a_(net.minecraft.item.Items.CROSSBOW, new ResourceLocation("pull"));
        IItemPropertyGetter charged = ItemModelsProperties.func_239417_a_(net.minecraft.item.Items.CROSSBOW, new ResourceLocation("charged"));
        IItemPropertyGetter firework = ItemModelsProperties.func_239417_a_(net.minecraft.item.Items.CROSSBOW, new ResourceLocation("firework"));
        registerPropertyGetter(Items.psimetalCrossbow, new ResourceLocation("pulling"), cpulling);
        registerPropertyGetter(Items.psimetalCrossbow, new ResourceLocation("pull"), cpull);
        registerPropertyGetter(Items.psimetalCrossbow, new ResourceLocation("charged"), charged);
        registerPropertyGetter(Items.psimetalCrossbow, new ResourceLocation("firework"), firework);
    }
    private void addCADModels(ModelRegistryEvent event) {
        ModelLoader.addSpecialModel(Psipherals.location("item/" + ItemNames.SWORD_CAD_PSIMETAL));
        ModelLoader.addSpecialModel(Psipherals.location("item/" + ItemNames.SWORD_CAD_EBONY_PSIMETAL));
        ModelLoader.addSpecialModel(Psipherals.location("item/" + ItemNames.SWORD_CAD_IVORY_PSIMETAL));
        ModelLoader.addSpecialModel(Psipherals.location("item/" + ItemNames.SWORD_CAD_CREATIVE));

        ModelLoader.addSpecialModel(Psipherals.location("item/" + ItemNames.PICKAXE_CAD_PSIMETAL));
        ModelLoader.addSpecialModel(Psipherals.location("item/" + ItemNames.PICKAXE_CAD_EBONY_PSIMETAL));
        ModelLoader.addSpecialModel(Psipherals.location("item/" + ItemNames.PICKAXE_CAD_IVORY_PSIMETAL));
        ModelLoader.addSpecialModel(Psipherals.location("item/" + ItemNames.PICKAXE_CAD_CREATIVE));

        ModelLoader.addSpecialModel(Psipherals.location("item/" + ItemNames.AXE_CAD_PSIMETAL));
        ModelLoader.addSpecialModel(Psipherals.location("item/" + ItemNames.AXE_CAD_EBONY_PSIMETAL));
        ModelLoader.addSpecialModel(Psipherals.location("item/" + ItemNames.AXE_CAD_IVORY_PSIMETAL));
        ModelLoader.addSpecialModel(Psipherals.location("item/" + ItemNames.AXE_CAD_CREATIVE));

        ModelLoader.addSpecialModel(Psipherals.location("item/igalima"));


    }
    private void loadComplete(FMLLoadCompleteEvent event) {
        DeferredWorkQueue.runLater(() -> {
            ItemColors items = Minecraft.getInstance().getItemColors();
            items.register((stack, tintIndex) -> tintIndex == 1 ? ((ItemSwordCad) stack.getItem()).getSpellColor(stack) : 0xFFFFFF, Items.swordCAD);
            items.register((stack, tintIndex) -> tintIndex == 1 ? ((ItemPickaxeCad) stack.getItem()).getSpellColor(stack) : 0xFFFFFF, Items.pickaxeCAD);
            items.register((stack, tintIndex) -> tintIndex == 1 ? ((ItemAxeCad) stack.getItem()).getSpellColor(stack) : 0xFFFFFF, Items.axeCAD);
        });
    }
}
