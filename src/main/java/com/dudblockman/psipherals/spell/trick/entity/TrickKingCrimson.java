package com.dudblockman.psipherals.spell.trick.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamEntity;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.core.handler.PlayerDataHandler;

public class TrickKingCrimson extends PieceTrick {

    SpellParam<Entity> target;
    SpellParam<Number> time;

    public TrickKingCrimson(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(target = new ParamEntity(SpellParam.GENERIC_NAME_TARGET, SpellParam.YELLOW, false, false));
        addParam(time = new ParamNumber(SpellParam.GENERIC_NAME_TIME, SpellParam.RED, false, true));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);
        Double timeVal = this.<Double>getParamEvaluation(time);
        if (timeVal == null) {
            timeVal = 1D;
        }

        meta.addStat(EnumSpellStat.POTENCY, (int) (Math.abs(timeVal) * 12));
        meta.addStat(EnumSpellStat.COST, (int) (Math.abs(timeVal) * 40));
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Entity targetVal = this.getParamValue(context, target);
        double timeVal = this.getParamValue(context, time).doubleValue();

        context.verifyEntity(targetVal);
        if (!context.isInRadius(targetVal)) {
            throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
        }

        Vector3d motion = targetVal.getMotion();
        if (targetVal instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) targetVal;
            PlayerDataHandler.PlayerData data = PlayerDataHandler.get(player);
            if (data.eidosChangelog.size() >= 2) {
                Vector3 last = data.eidosChangelog.get(data.eidosChangelog.size() - 2);
                Vector3 vec = Vector3.fromEntity(targetVal).sub(last);
                if (vec.mag() < 10) {
                    motion = new Vector3d(vec.x, vec.y, vec.z);
                }
            }
        }
        double offX = motion.x * timeVal;
        double offY = motion.y * timeVal;
        double offZ = motion.z * timeVal;
        int adjustY = 0;
        for (int i = -(int) offY; i > 0; i--) {
            BlockPos pos = new BlockPos(targetVal.getPosX() + offX, targetVal.getPosY() + offY + adjustY, targetVal.getPosZ() + offZ);
            BlockState state = context.caster.getEntityWorld().getBlockState(pos);
            if (state.isAir(context.caster.getEntityWorld(), pos) || state.getMaterial().isReplaceable()) {
                break;
            }
            adjustY++;
        }

        targetVal.setPositionAndUpdate(targetVal.getPosX() + offX, adjustY == 0 ? targetVal.getPosY() + offY + adjustY : (int) (targetVal.getPosY() + offY + adjustY), targetVal.getPosZ() + offZ);
        return null;
    }
}
