package com.dudblockman.psipherals.spell.operator.overrides;

import vazkii.psi.api.internal.Vector3;
import vazkii.psi.api.spell.Spell;
import vazkii.psi.api.spell.SpellCompilationException;
import vazkii.psi.api.spell.SpellParam;
import vazkii.psi.api.spell.SpellPiece;
import vazkii.psi.api.spell.piece.PieceOperator;

import java.util.HashSet;
import java.util.Set;

public abstract class OperatorAdaptiveBase extends PieceOperator {

    public SpellPiece orignalPiece;
    public Double defaultValue = 0D;

    public OperatorAdaptiveBase(Spell spell) {
        super(spell);
    }

    public boolean isInfiniteLoop() {
        return isInfiniteLoop(this, new HashSet<>());
    }

    public boolean isInfiniteLoop(SpellPiece piece, Set<SpellPiece> visited) {
        if (!visited.add(piece)) {
            return true;
        }
        for (SpellParam.Side side : piece.paramSides.values()) {
            try {
                SpellPiece nextPiece = spell.grid.getPieceAtSideWithRedirections(piece.x, piece.y, side);
                if (nextPiece != piece) {
                    if (nextPiece instanceof OperatorAdaptiveBase && isInfiniteLoop(nextPiece, visited)) {
                        return true;
                    }
                }
            } catch (SpellCompilationException exception) {
                //return true;
            }
        }
        visited.remove(piece);
        return false;
    }

    @Override
    public Class<?> getEvaluationType() {
        boolean isVector = false;
        boolean isNumber = false;
        if (!isInfiniteLoop()) {
            for (SpellParam<?> param : this.paramSides.keySet()) {
                SpellParam.Side side = this.paramSides.get(param);
                SpellPiece piece = null;
                try {
                    piece = spell.grid.getPieceAtSideWithRedirections(this.x, this.y, side);
                } catch (SpellCompilationException ignored) {
                }
                if (piece != null && piece != this) {
                    Class<?> type = piece.getEvaluationType();
                    if (Vector3.class.isAssignableFrom(type)) {
                        isVector = true;
                    } else if (Number.class.isAssignableFrom(type)) {
                        isNumber = true;
                    }
                }
            }
        }
        return isVector ? Vector3.class : (isNumber ? Double.class : OperatorAdaptiveSum.VectorOrNumber.class);
    }

    public Vector3 vectorize(Object object) {
        if (object instanceof Vector3) {
            return (Vector3) object;
        } else if (object instanceof Number) {
            double val = ((Number) object).doubleValue();
            return new Vector3(val, val, val);
        }
        return new Vector3(defaultValue, defaultValue, defaultValue);
    }

    public static class ParamVectorNumber extends SpellParam<VectorOrNumber> {
        public ParamVectorNumber(String name, int color, boolean canDisable) {
            super(name, color, canDisable);
        }

        @Override
        protected Class<VectorOrNumber> getRequiredType() {
            return VectorOrNumber.class;
        }

        @Override
        public boolean canAccept(SpellPiece piece) {
            return (Number.class.isAssignableFrom(piece.getEvaluationType()) || Vector3.class.isAssignableFrom(piece.getEvaluationType()));
        }

    }

    public static class VectorOrNumber {
    }
}
