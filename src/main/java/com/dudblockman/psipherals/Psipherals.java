package com.dudblockman.psipherals;

import com.dudblockman.psipherals.items.Items;
//import com.dudblockman.psipherals.spell.base.SpellPieces;
import com.dudblockman.psipherals.util.CreativeTab;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import vazkii.psi.common.lib.LibMisc;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

@Mod("psipherals")
public class Psipherals {
    public static final String MODID = "psipherals";
    public static final String NAME = "Psionic Peripherals";
    public static final String VERSION = "%VERSION%";
    //public static final Logger LOGGER = LogManager.getLogger(MODID);

    public static Psipherals INSTANCE;

    public Psipherals() {
        INSTANCE = this;
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        //new CreativeTab();
        new Items();
        //SpellPieces.init();
    }
    public static ResourceLocation location(String path) {
        return new ResourceLocation(MODID, path);
    }
}
