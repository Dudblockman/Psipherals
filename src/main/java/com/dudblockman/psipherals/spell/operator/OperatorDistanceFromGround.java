package com.dudblockman.psipherals.spell.operator;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.common.extensions.IForgeBlockState;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceOperator;
import vazkii.psi.common.core.helpers.SpellHelpers;

public class OperatorDistanceFromGround extends PieceOperator {

    private SpellParam<Vector3> target;

    public OperatorDistanceFromGround(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(target = new ParamVector(SpellParam.GENERIC_NAME_TARGET, SpellParam.BLUE, false, false));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Vector3 targetVal = SpellHelpers.getVector3(this, context, target, true, false, false);
        if (targetVal == null)
            throw new SpellRuntimeException(SpellRuntimeException.NULL_TARGET);

        World world = context.caster.world;
        BlockPos pos = targetVal.toBlockPos();

        IChunk chunk = world.getChunk(pos);
        BlockPos pointer = new BlockPos(pos.getX(), Math.min(chunk.getTopFilledSegment() + 16, pos.getY()), pos.getZ());

        if (targetVal.y < 0)
            throw new SpellRuntimeException(SpellRuntimeException.NEGATIVE_NUMBER);
        else
            while (pointer.getY() >= 0) {
                pointer = pointer.down();
                IForgeBlockState state = chunk.getBlockState(pointer);
                if (!state.isAir(world,pointer)) {
                    break;
                }
            }

        return targetVal.y - (pointer.getY() + 1);
    }

    @Override
    public Class<?> getEvaluationType() {
        return Double.class;
    }
}
