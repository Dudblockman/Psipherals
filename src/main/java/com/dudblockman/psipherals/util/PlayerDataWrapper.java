package com.dudblockman.psipherals.util;

public interface PlayerDataWrapper {
    void stepBurnout();

    void addBurnout(int burnout);

    void subtractBurnout(int burnout);

    void getActivePsilon();

    void PsilonUpdate();
}
