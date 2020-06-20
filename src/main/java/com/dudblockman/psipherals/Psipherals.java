package com.dudblockman.psipherals;

import com.dudblockman.psipherals.items.Items;
import com.dudblockman.psipherals.items.StatRegistry;
import com.dudblockman.psipherals.util.ClientBakery;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

//import com.dudblockman.psipherals.spell.base.SpellPieces;
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
        ClientBakery proxy = DistExecutor.runForDist(() -> ClientBakery::new, null);
        if (proxy != null)
            proxy.registerHandlers();
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        //new CreativeTab();
        new Items();
        StatRegistry.registerStats();
        //SpellPieces.init();
    }
    public static ResourceLocation location(String path) {
        return new ResourceLocation(MODID, path);
    }
}
