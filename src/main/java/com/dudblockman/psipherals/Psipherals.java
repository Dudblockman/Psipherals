package com.dudblockman.psipherals;

import com.dudblockman.psipherals.entity.Entities;
import com.dudblockman.psipherals.items.Items;
import com.dudblockman.psipherals.items.StatRegistry;
import com.dudblockman.psipherals.spell.base.SpellPieces;
import com.dudblockman.psipherals.util.ClientProxy;
import com.dudblockman.psipherals.util.IProxy;
import com.dudblockman.psipherals.util.ServerProxy;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotTypeMessage;

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
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        MinecraftForge.EVENT_BUS.register(this);
        IProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);
        proxy.registerHandlers();
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        new Items();
        new Entities();
        StatRegistry.registerStats();
        SpellPieces.init();
    }
    public static ResourceLocation location(String path) {
        return new ResourceLocation(MODID, path);
    }

    public void enqueueIMC(InterModEnqueueEvent evt) {
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> new SlotTypeMessage.Builder("necklace").build());
    }
}
