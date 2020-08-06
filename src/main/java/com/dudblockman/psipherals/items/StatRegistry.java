package com.dudblockman.psipherals.items;

import vazkii.psi.api.cad.EnumCADStat;
import vazkii.psi.common.item.component.ItemCADComponent;

public class StatRegistry {
    public static void registerStats() {
        registerAssemblyStats();
    }
    public static void registerAssemblyStats() {
        ItemCADComponent.addStatToStack(Items.swordAssemblyPsimetal, EnumCADStat.EFFICIENCY,85);
        ItemCADComponent.addStatToStack(Items.swordAssemblyPsimetal, EnumCADStat.POTENCY,250);
        ItemCADComponent.addStatToStack(Items.pickaxeAssemblyPsimetal, EnumCADStat.EFFICIENCY,85);
        ItemCADComponent.addStatToStack(Items.pickaxeAssemblyPsimetal, EnumCADStat.POTENCY,250);
        ItemCADComponent.addStatToStack(Items.shovelAssemblyPsimetal, EnumCADStat.EFFICIENCY,85);
        ItemCADComponent.addStatToStack(Items.shovelAssemblyPsimetal, EnumCADStat.POTENCY,250);
        ItemCADComponent.addStatToStack(Items.axeAssemblyPsimetal, EnumCADStat.EFFICIENCY,85);
        ItemCADComponent.addStatToStack(Items.axeAssemblyPsimetal, EnumCADStat.POTENCY,250);

        ItemCADComponent.addStatToStack(Items.swordAssemblyIvoryPsimetal, EnumCADStat.EFFICIENCY,95);
        ItemCADComponent.addStatToStack(Items.swordAssemblyIvoryPsimetal, EnumCADStat.POTENCY,320);
        ItemCADComponent.addStatToStack(Items.pickaxeAssemblyIvoryPsimetal, EnumCADStat.EFFICIENCY,95);
        ItemCADComponent.addStatToStack(Items.pickaxeAssemblyIvoryPsimetal, EnumCADStat.POTENCY,320);
        ItemCADComponent.addStatToStack(Items.shovelAssemblyIvoryPsimetal, EnumCADStat.EFFICIENCY,95);
        ItemCADComponent.addStatToStack(Items.shovelAssemblyIvoryPsimetal, EnumCADStat.POTENCY,320);
        ItemCADComponent.addStatToStack(Items.axeAssemblyIvoryPsimetal, EnumCADStat.EFFICIENCY,95);
        ItemCADComponent.addStatToStack(Items.axeAssemblyIvoryPsimetal, EnumCADStat.POTENCY,320);

        ItemCADComponent.addStatToStack(Items.swordAssemblyEbonyPsimetal, EnumCADStat.EFFICIENCY,90);
        ItemCADComponent.addStatToStack(Items.swordAssemblyEbonyPsimetal, EnumCADStat.POTENCY,350);
        ItemCADComponent.addStatToStack(Items.pickaxeAssemblyEbonyPsimetal, EnumCADStat.EFFICIENCY,90);
        ItemCADComponent.addStatToStack(Items.pickaxeAssemblyEbonyPsimetal, EnumCADStat.POTENCY,350);
        ItemCADComponent.addStatToStack(Items.shovelAssemblyEbonyPsimetal, EnumCADStat.EFFICIENCY,90);
        ItemCADComponent.addStatToStack(Items.shovelAssemblyEbonyPsimetal, EnumCADStat.POTENCY,350);
        ItemCADComponent.addStatToStack(Items.axeAssemblyEbonyPsimetal, EnumCADStat.EFFICIENCY,90);
        ItemCADComponent.addStatToStack(Items.axeAssemblyEbonyPsimetal, EnumCADStat.POTENCY,350);

        ItemCADComponent.addStatToStack(Items.swordAssemblyCreative, EnumCADStat.EFFICIENCY,-1);
        ItemCADComponent.addStatToStack(Items.swordAssemblyCreative, EnumCADStat.POTENCY,-1);
        ItemCADComponent.addStatToStack(Items.pickaxeAssemblyCreative, EnumCADStat.EFFICIENCY,-1);
        ItemCADComponent.addStatToStack(Items.pickaxeAssemblyCreative, EnumCADStat.POTENCY,-1);
        ItemCADComponent.addStatToStack(Items.shovelAssemblyCreative, EnumCADStat.EFFICIENCY,-1);
        ItemCADComponent.addStatToStack(Items.shovelAssemblyCreative, EnumCADStat.POTENCY,-1);
        ItemCADComponent.addStatToStack(Items.axeAssemblyCreative, EnumCADStat.EFFICIENCY,-1);
        ItemCADComponent.addStatToStack(Items.axeAssemblyCreative, EnumCADStat.POTENCY,-1);


        ItemCADComponent.addStatToStack(Items.dessAssembly, EnumCADStat.EFFICIENCY,90);
        ItemCADComponent.addStatToStack(Items.dessAssembly, EnumCADStat.POTENCY,350);

    }
}
