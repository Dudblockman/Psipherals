package com.dudblockman.psipherals;

import com.dudblockman.psipherals.items.Items;
import com.dudblockman.psipherals.spell.base.SpellPieces;
import com.dudblockman.psipherals.util.CreativeTab;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = Psipherals.MODID, name = Psipherals.NAME, version = Psipherals.VERSION, dependencies = "required-after:psi@[r1.1-75,);required-after:librarianlib;required-after:forge@[14.23.5.2768,);", useMetadata = true)
public class Psipherals {
    public static final String MODID = "psipherals";
    public static final String NAME = "Psionic Peripherals";
    public static final String VERSION = "%VERSION%";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    @Mod.Instance
    public static Psipherals INSTANCE;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        new CreativeTab();
        new Items();
        SpellPieces.init();
    }
}
