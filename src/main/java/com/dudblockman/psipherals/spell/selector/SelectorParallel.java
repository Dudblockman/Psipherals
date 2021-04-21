package com.dudblockman.psipherals.spell.selector;

import com.dudblockman.psipherals.spell.operator.OperatorParallel;
import vazkii.psi.api.spell.*;
import vazkii.psi.api.spell.piece.PieceSelector;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public abstract class SelectorParallel extends PieceSelector {
    public SelectorParallel(Spell spell) {
        super(spell);
    }
    public boolean TriggerParallel(SpellContext context) throws SpellRuntimeException {
        OperatorParallel operator = getParallelOperator(this, context);
        if (operator != null) {
            operator.hackTheStack(context);
            return true;
        }
        return false;
    }
    protected OperatorParallel getParallelOperator(SpellPiece start, SpellContext context) throws SpellRuntimeException {
        Set<SpellPiece> visited = new HashSet<>();
        Queue<SpellPiece> queue = new LinkedList<SpellPiece>();

        queue.add(start);

        OperatorParallel parallelPiece = null;
        while (!queue.isEmpty()) {
            SpellPiece current = queue.remove();
            if (visited.contains(current)) {
                continue;
            }
            visited.add(current);
            for (SpellParam.Side s : SpellParam.Side.values()) {
                SpellPiece next = context.cspell.sourceSpell.grid.getPieceAtSideSafely(current.x, current.y, s);
                if (next != null) {
                    boolean connected = false;
                    for (SpellParam<?> param : next.paramSides.keySet()) {
                        SpellParam.Side side = next.paramSides.get(param);

                        SpellPiece pieceAt = context.cspell.sourceSpell.grid.getPieceAtSideSafely(next.x, next.y, side);

                        if (pieceAt == current) {
                            connected = true;
                            break;
                        }
                    }
                    if (!connected) {
                        continue;
                    }
                    if (next instanceof OperatorParallel) {
                        if (parallelPiece != null && !visited.contains(parallelPiece)) {
                            throw new SpellRuntimeException("Used in multiple parallel contexts");
                        }
                        parallelPiece = (OperatorParallel) next;
                    } else {
                        queue.add(next);
                    }
                }
            }
        }
        if (parallelPiece == null) {
            throw new SpellRuntimeException("Used outside parallel context");
        }
        return parallelPiece;
    }
}
