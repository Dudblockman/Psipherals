package com.dudblockman.psipherals.spell.trick;

import net.minecraft.item.ItemStack;
import vazkii.psi.api.PsiAPI;
import vazkii.psi.api.cad.ICAD;
import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.param.ParamVector;
import vazkii.psi.api.spell.piece.PieceTrick;

public class PieceTrickSaveBinary extends PieceTrick {

    public static final String KEY_BINARY_LOCKED = "psipherals:BinaryLocked";
    public static final int MAX_BITS_PER_OPERATION = 8;

    SpellParam position;
    SpellParam band;
    SpellParam target;

    public PieceTrickSaveBinary(Spell spell){
        super(spell);
    }

    @Override
    public void initParams() {
        addParam(position = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER, SpellParam.BLUE, false, true));
        addParam(band = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER, SpellParam.PURPLE, true, true));

        addParam(target = new ParamNumber(SpellParam.GENERIC_NAME_TARGET, SpellParam.RED, false, false));
    }

    @Override
    public void addToMetadata(SpellMetadata meta) throws SpellCompilationException {

        Double posVal = this.<Double>getParamEvaluation(position);
        if(posVal == null || posVal <= 0 || posVal != posVal.intValue())
            throw new SpellCompilationException(SpellCompilationException.NON_POSITIVE_INTEGER, x, y);

        Double bandVal = this.<Double>getParamEvaluation(band);
        if (bandVal == null) {
            bandVal = 0d;
        }
        if(bandVal == null || bandVal <= 0 || bandVal != bandVal.intValue())
            throw new SpellCompilationException(SpellCompilationException.NON_INTEGER, x, y);

        meta.addStat(EnumSpellStat.COMPLEXITY, 1);
        meta.addStat(EnumSpellStat.POTENCY, (posVal.intValue() / 16 + 1) * 8);
    }

    @Override
    public Object execute(SpellContext context) throws SpellRuntimeException {
        Double numberVal = this.<Double>getParamValue(context, position);
        Double targetVal = this.<Double>getParamValue(context, target);

        int n = numberVal.intValue() - 1;

        if(context.customData.containsKey(KEY_BINARY_LOCKED + n))
            return null;

        ItemStack cadStack = PsiAPI.getPlayerCAD(context.caster);
        if(cadStack == null || !(cadStack.getItem() instanceof ICAD))
            throw new SpellRuntimeException(SpellRuntimeException.NO_CAD);

        ICAD cad = (ICAD) cadStack.getItem();

        //cad.setStoredVector(cadStack, n, targetVal);

        context.customData.put(KEY_BINARY_LOCKED + n, 0);

        return null;
    }
}
