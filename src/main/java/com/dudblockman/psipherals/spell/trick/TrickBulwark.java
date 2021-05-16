package com.dudblockman.psipherals.spell.trick;

import com.dudblockman.psipherals.potions.Potions;
import net.minecraft.potion.Effect;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.common.spell.trick.potion.PieceTrickPotionBase;

public class TrickBulwark extends PieceTrickPotionBase {
    public TrickBulwark(Spell spell) {
        super(spell);
    }

    @Override
    public Effect getPotion() {
        return Potions.bulwark;
    }
}
