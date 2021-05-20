package com.dudblockman.psipherals.spell.trick;

import com.dudblockman.psipherals.block.tile.TilePsilon;
import com.dudblockman.psipherals.crafting.InfusionCraftingHelper;
import net.minecraft.tileentity.TileEntity;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

public class TrickPsilon extends PieceTrick {
    SpellParam<Vector3> position;
    SpellParam<Vector3> frequency;

    public TrickPsilon(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
        addParam(frequency = new ParamVector(SpellParam.GENERIC_NAME_VECTOR1, SpellParam.GREEN, false, false));
    }
    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);
        meta.addStat(EnumSpellStat.POTENCY, 35);
        meta.addStat(EnumSpellStat.COST, 25);
    }
    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Vector3 positionVal = this.getParamValue(context, position);
        Vector3 frequencyVal = this.getParamValue(context, frequency);

        if (positionVal == null || frequencyVal == null) {
            throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
        }

        if (!context.isInRadius(positionVal)) {
            throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
        }
        TileEntity target = context.focalPoint.world.getTileEntity(positionVal.toBlockPos());
        if (target instanceof TilePsilon) {
            TilePsilon master = (TilePsilon) target;
            switch (InfusionCraftingHelper.invokePsilon(master,frequencyVal)) {
                case MULTIPLE_RADIUS:
                    throw new SpellRuntimeException("MULTIPLE RADII");
                case UNBALANCED_PLACEMENT:
                    throw new SpellRuntimeException("BAD DISTRIBUTION");
            }
        }
        return null;
    }

}
