package com.dudblockman.psipherals.spell.trick.entity;

import net.minecraft.entity.Entity;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamEntity;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.core.handler.AdditiveMotionHandler;


public class PieceTrickRedirectMotion extends PieceTrick {
    SpellParam target;
    SpellParam axis;
    SpellParam angle;
    public PieceTrickRedirectMotion(Spell spell){
        super(spell);
    }
    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {
        super.addToMetadata(meta);
        Double angleVal = this.<Double>getParamEvaluation(angle);
        if(angleVal == null)
            angleVal = 1D;

        double absAngle = Math.abs(angleVal);
        meta.addStat(EnumSpellStat.POTENCY, (int) multiplySafe(absAngle, absAngle, 25));
        meta.addStat(EnumSpellStat.COST, (int) multiplySafe(absAngle, 100));
    }
    @Override
    public void initParams() {
        addParam(target = new ParamEntity(SpellParam.GENERIC_NAME_TARGET, SpellParam.YELLOW, false, false));
        addParam(axis = new ParamVector("psi.spellparam.direction", SpellParam.GREEN, false, false));
        addParam(angle = new ParamNumber("psi.spellparam.speed", SpellParam.RED, false, true));
    }
    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Entity targetVal = this.getParamValue(context, target);
        Vector3 axisVal = this.getParamValue(context, axis);
        Double angleVal = this.<Double>getParamValue(context, angle);

        addMotion(context, targetVal, axisVal, angleVal);

        return null;
    }

    public static void addMotion(SpellContext context, Entity e, Vector3 ax, double ang) throws SpellRuntimeException {
        context.verifyEntity(e);
        if(!context.isInRadius(e))
            throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
        Vector3 entityVelocity = new Vector3(e.motionX, e.motionY, e.motionZ);
        Vector3 newMotion = entityVelocity.copy().rotate(ang, ax).subtract(entityVelocity);

        String key = "psi:Entity" + e.getEntityId() + "Motion";

        double x = 0;
        double y = 0;
        double z = 0;

        if(Math.abs(newMotion.x) > 0.0001) {
            String keyv = key + "X";
            if(!context.customData.containsKey(keyv)) {
                x += newMotion.x;
                context.customData.put(keyv, 0);
            }
        }

        if(Math.abs(newMotion.y) > 0.0001) {
            String keyv = key + "Y";
            if(!context.customData.containsKey(keyv)) {
                y += newMotion.y;
                context.customData.put(keyv, 0);
            }

            if(e.motionY >= 0)
                e.fallDistance = 0;
        }

        if(Math.abs(newMotion.z) > 0.0001) {
            String keyv = key + "Z";
            if(!context.customData.containsKey(keyv)) {
                z += newMotion.z;
                context.customData.put(keyv, 0);
            }
        }

        AdditiveMotionHandler.addMotion(e, x, y, z);
    }
}
