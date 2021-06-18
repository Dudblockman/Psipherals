package com.dudblockman.psipherals.util;

import com.dudblockman.psipherals.block.tile.TilePsilon;

public interface PlayerDataWrapper {
    void stepBurnout();

    void addBurnout(int burnout);

    void subtractBurnout(int burnout);

    TilePsilon getActivePsilon();

    void psilonUpdate();

    int getLoopcastRate(int original);

    void setLoopcastRate(int rate);
}
