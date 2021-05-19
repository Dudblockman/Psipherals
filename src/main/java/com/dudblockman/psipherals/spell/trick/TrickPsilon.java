package com.dudblockman.psipherals.spell.trick;

import com.dudblockman.psipherals.block.tile.TilePsilon;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunk;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
            ((TilePsilon) target).activate(frequencyVal);
            System.out.println(target);
            //List<TilePsilon> Psilons = getTilesWithinAABB(TilePsilon.class, context.focalPoint.world, AxisAlignedBB.fromVector(context.focalPoint.getPositionVec()).grow(SpellContext.MAX_DISTANCE));
            //System.out.println(Psilons.size());
        }

        return null;
    }

    public static <T> List<T> getTilesWithinAABB(Class<T> type, World world, AxisAlignedBB bb) {
        List<T> tileList = new ArrayList<>();
        for (int i = (int)Math.floor(bb.minX); i < (int)Math.ceil(bb.maxX) + 16; i += 16) {
            for (int j = (int)Math.floor(bb.minZ); j < (int)Math.ceil(bb.maxZ) + 16; j += 16) {
                IChunk c = world.getChunk(new BlockPos(i, 0, j));
                Set<BlockPos> tiles = c.getTileEntitiesPos();
                for (BlockPos p : tiles) if (bb.contains(p.getX() + 0.5, p.getY() + 0.5, p.getZ() + 0.5)) {
                    TileEntity t = world.getTileEntity(p);
                    if (type.isInstance(t)) tileList.add((T)t);
                }
            }
        }
        return tileList;
    }
}
