package com.dudblockman.psipherals.spell.trick;

import com.dudblockman.psipherals.potions.Potions;
import net.minecraft.potion.Effect;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.spell.trick.potion.PieceTrickPotionBase;

public class TrickSturdy extends PieceTrickPotionBase {
    public TrickSturdy(Spell spell) {
        super(spell);
    }

    @Override
    public Effect getPotion() {
        return Potions.sturdy;
    }
}
