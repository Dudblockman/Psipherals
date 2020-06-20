package com.dudblockman.psipherals.util;

import com.dudblockman.psipherals.Psipherals;
import com.dudblockman.psipherals.items.Items;
import com.dudblockman.psipherals.util.libs.ItemNames;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import vazkii.psi.client.model.ModelCAD;

import java.util.Objects;

@OnlyIn(Dist.CLIENT)
public class ClientBakery {

    public void registerHandlers() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::modelBake);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::addCADModels);
    }

    private void clientSetup(FMLClientSetupEvent event) {
        //ModelBakery.LOCATIONS_BUILTIN_TEXTURES.addAll(PsiAPI.getAllSpellPieceMaterial());
    }
    private void modelBake(ModelBakeEvent event) {
        Item[] cads = {Items.swordCAD,Items.pickaxeCAD,Items.shovelCAD,Items.axeCAD};
        for (Item cad : cads) {
            ModelResourceLocation key = new ModelResourceLocation(Objects.requireNonNull(cad.getRegistryName()), "inventory");
            event.getModelRegistry().put(key, new ModelCAD());
        }
    }

    private void addCADModels(ModelRegistryEvent event) {
        ModelLoader.addSpecialModel(Psipherals.location("item/" + ItemNames.SWORD_CAD_IRON));
        ModelLoader.addSpecialModel(Psipherals.location("item/" + ItemNames.SWORD_CAD_GOLD));
        ModelLoader.addSpecialModel(Psipherals.location("item/" + ItemNames.SWORD_CAD_PSIMETAL));
        ModelLoader.addSpecialModel(Psipherals.location("item/" + ItemNames.SWORD_CAD_EBONY_PSIMETAL));
        ModelLoader.addSpecialModel(Psipherals.location("item/" + ItemNames.SWORD_CAD_IVORY_PSIMETAL));
        ModelLoader.addSpecialModel(Psipherals.location("item/" + ItemNames.SWORD_CAD_CREATIVE));

        ModelLoader.addSpecialModel(Psipherals.location("item/" + ItemNames.PICKAXE_CAD_IRON));
        ModelLoader.addSpecialModel(Psipherals.location("item/" + ItemNames.PICKAXE_CAD_GOLD));
        ModelLoader.addSpecialModel(Psipherals.location("item/" + ItemNames.PICKAXE_CAD_PSIMETAL));
        ModelLoader.addSpecialModel(Psipherals.location("item/" + ItemNames.PICKAXE_CAD_EBONY_PSIMETAL));
        ModelLoader.addSpecialModel(Psipherals.location("item/" + ItemNames.PICKAXE_CAD_IVORY_PSIMETAL));
        ModelLoader.addSpecialModel(Psipherals.location("item/" + ItemNames.PICKAXE_CAD_CREATIVE));

        ModelLoader.addSpecialModel(Psipherals.location("item/" + ItemNames.SHOVEL_CAD_IRON));
        ModelLoader.addSpecialModel(Psipherals.location("item/" + ItemNames.SHOVEL_CAD_GOLD));
        ModelLoader.addSpecialModel(Psipherals.location("item/" + ItemNames.SHOVEL_CAD_PSIMETAL));
        ModelLoader.addSpecialModel(Psipherals.location("item/" + ItemNames.SHOVEL_CAD_EBONY_PSIMETAL));
        ModelLoader.addSpecialModel(Psipherals.location("item/" + ItemNames.SHOVEL_CAD_IVORY_PSIMETAL));
        ModelLoader.addSpecialModel(Psipherals.location("item/" + ItemNames.SHOVEL_CAD_CREATIVE));

        ModelLoader.addSpecialModel(Psipherals.location("item/" + ItemNames.AXE_CAD_IRON));
        ModelLoader.addSpecialModel(Psipherals.location("item/" + ItemNames.AXE_CAD_GOLD));
        ModelLoader.addSpecialModel(Psipherals.location("item/" + ItemNames.AXE_CAD_PSIMETAL));
        ModelLoader.addSpecialModel(Psipherals.location("item/" + ItemNames.AXE_CAD_EBONY_PSIMETAL));
        ModelLoader.addSpecialModel(Psipherals.location("item/" + ItemNames.AXE_CAD_IVORY_PSIMETAL));
        ModelLoader.addSpecialModel(Psipherals.location("item/" + ItemNames.AXE_CAD_CREATIVE));


    }
}
