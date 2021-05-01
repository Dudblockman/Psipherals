package com.dudblockman.psipherals.spell.trick;

import net.minecraft.item.ItemStack;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.EnumCADComponent;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellContext;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellRuntimeException;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;
import vazkii.psi.common.entity.EntitySpellProjectile;


public class TrickFork extends PieceTrick {
    SpellParam<Vector3> position;
    SpellParam<Vector3> direction;
    public TrickFork(Spell spell) {
        super(spell);
    }
    @Override
    public void initParams() {
        addParam(position = new ParamVector(SpellParam.GENERIC_NAME_POSITION, SpellParam.BLUE, false, false));
        addParam(direction = new ParamVector(SpellParam.GENERIC_NAME_DIRECTION, SpellParam.GREEN, false, false));
    }
    private SpellContext copyContext(SpellContext original) {
        SpellContext newContext = new SpellContext();
        newContext.caster = original.caster;
        newContext.focalPoint = original.focalPoint;
        newContext.cspell = original.cspell;
        newContext.loopcastIndex = original.loopcastIndex;
        newContext.castFrom = original.castFrom;
        newContext.tool = original.tool;
        newContext.positionBroken = original.positionBroken;
        newContext.attackedEntity = original.attackedEntity;
        newContext.attackingEntity = original.attackingEntity;
        newContext.damageTaken = original.damageTaken;
        newContext.targetSlot = original.targetSlot;
        newContext.shiftTargetSlot = original.shiftTargetSlot;
        newContext.customTargetSlot = original.customTargetSlot;
        newContext.actions = original.actions;
        newContext.stopped = original.stopped;
        newContext.delay = original.delay;
        newContext.customData.putAll(original.customData);
        for (int i = 0; i < 9; i++) {
            System.arraycopy(original.evaluatedObjects[i], 0, newContext.evaluatedObjects[i], 0, 9);
        }
        return newContext;
    }
    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Vector3 positionVal = this.getParamValue(context, position);
        Vector3 directionVal = this.getParamValue(context, direction);

        if (positionVal == null || directionVal == null || directionVal.mag() == 0) {
            throw new SpellRuntimeException(SpellRuntimeException.NULL_VECTOR);
        }

        if (!context.isInRadius(positionVal)) {
            throw new SpellRuntimeException(SpellRuntimeException.OUTSIDE_RADIUS);
        }

        ItemStack cad = PsiAPI.getPlayerCAD(context.caster);
        ItemStack colorizer = ((ICAD) cad.getItem()).getComponentInSlot(cad, EnumCADComponent.DYE);
        EntitySpellProjectile projectile = new EntitySpellProjectile(context.caster.getEntityWorld(), context.caster);
        projectile.setInfo(context.caster, colorizer, ItemStack.EMPTY); //May have issues
        projectile.context = copyContext(context); //TODO use copy constructor
        projectile.getEntityWorld().addEntity(projectile);
        float f = 1.5F;
        Vector3 motion = directionVal.normalize().multiply(f);
        projectile.setPosition(positionVal.x,positionVal.y,positionVal.z);
        projectile.setVelocity(motion.x, motion.y, motion.z);
        return null;
    }
}
