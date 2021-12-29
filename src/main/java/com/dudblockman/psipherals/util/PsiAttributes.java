package com.dudblockman.psipherals.util;

import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.util.registry.Registry;

public class PsiAttributes {
    public static final Attribute PSI_CAPACITY = Registry.register(Registry.ATTRIBUTE, "psipherals.psicapacity", new RangedAttribute("attribute.name.psipherals.psicapacity", 5000D, 0.0D, 10000D));
    public static final Attribute PSI_REGEN = Registry.register(Registry.ATTRIBUTE, "psipherals.psiregen", new RangedAttribute("attribute.name.psipherals.psiregen", 25D, 0.0D, 100D));
}
