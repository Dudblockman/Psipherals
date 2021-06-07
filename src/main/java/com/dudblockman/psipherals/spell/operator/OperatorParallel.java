package com.dudblockman.psipherals.spell.operator;

import com.dudblockman.psipherals.spell.selector.SelectorParallel;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.CompiledSpell.Action;
import vazkii.psi.api.spell.param.ParamEntityListWrapper;
import vazkii.psi.api.spell.param.ParamNumber;
import vazkii.psi.api.spell.piece.PieceOperator;
import vazkii.psi.api.spell.wrapper.EntityListWrapper;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

public abstract class OperatorParallel extends PieceOperator {
    public static final String ENTITY = "psipherals.parallelentity";
    public static final String INDEX = "psipherals.parallelindex";
    public Stack<Action> repeatedActions;
    public Vector<Double> inputs;
    SpellParam<EntityListWrapper> entList;
    SpellParam<Number> target;

    public OperatorParallel(Spell spell) {
        super(spell);
    }

    @Override
    public void initParams() {
        super.initParams();
        addParam(entList = new ParamEntityListWrapper(SpellParam.GENERIC_NAME_LIST, SpellParam.GREEN, false, false));
        addParam(target = new ParamNumber(SpellParam.GENERIC_NAME_NUMBER, SpellParam.BLUE, false, false));
    }

    public boolean processExecution(EntityListWrapper listVal, SpellContext context) throws SpellRuntimeException {
        if (!context.customData.containsKey(OperatorParallel.INDEX)) {
            if (listVal.size() == 0) {
                return true;
            }
            CompiledSpell.Action thisAction = null;
            for (CompiledSpell.Action a : context.cspell.actions) {
                if (a.piece == this) {
                    thisAction = a;
                    break;
                }
            }
            if (thisAction == null) {
                throw new SpellRuntimeException("H O W");
            }
            for (int i = 0; i < listVal.size(); i++) {
                context.actions.push(thisAction);
                for (CompiledSpell.Action a : this.repeatedActions) {
                    context.actions.push(a);
                }
            }
            inputs = new Vector<Double>();
            context.customData.put(OperatorParallel.ENTITY, listVal.get(0));
            context.customData.put(OperatorParallel.INDEX, 0);
        } else {
            double input = this.getParamValue(context, target).doubleValue();
            inputs.add(input);
            int index = (int) context.customData.get(OperatorParallel.INDEX);
            if (index + 1 < listVal.size()) {
                context.customData.put(OperatorParallel.ENTITY, listVal.get(index + 1));
                context.customData.put(OperatorParallel.INDEX, index + 1);
            } else {
                context.customData.remove(OperatorParallel.ENTITY);
                context.customData.remove(OperatorParallel.INDEX);
                return true;
            }
        }
        return false;
    }

    public void hackTheStack(SpellContext context) {
        Set<SpellPiece> updatePieces = new HashSet<>();
        SpellPiece updatedPiece = context.cspell.sourceSpell.grid.getPieceAtSideSafely(this.x, this.y, this.paramSides.get(target));
        depthFirstSearch(updatedPiece, new HashSet<>(), updatePieces, context);
        updatePieces.remove(this);
        repeatedActions = new Stack<>();
        for (Action a : context.cspell.actions) {
            if (updatePieces.contains(a.piece)) {
                context.actions.remove(a);
                repeatedActions.push(a);
            }
        }
    }

    protected boolean depthFirstSearch(SpellPiece piece, Set<SpellPiece> visited, Set<SpellPiece> found, SpellContext context) {
        boolean isUpdated = piece instanceof SelectorParallel;
        if (!visited.add(piece) || piece instanceof OperatorParallel) {
            return isUpdated;
        }

        for (SpellParam<?> param : piece.paramSides.keySet()) {
            SpellParam.Side side = piece.paramSides.get(param);

            SpellPiece pieceAt = context.cspell.sourceSpell.grid.getPieceAtSideSafely(piece.x, piece.y, side);

            isUpdated |= depthFirstSearch(pieceAt, visited, found, context);
        }
        if (isUpdated) {
            found.add(piece);
        }
        return isUpdated;
    }
}
