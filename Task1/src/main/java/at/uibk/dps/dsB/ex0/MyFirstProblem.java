package at.uibk.dps.dsB.ex0;

import com.google.inject.Inject;
import org.opt4j.core.start.Constant;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MyFirstProblem {
    protected int numberCoins;
    protected int targetValue;
    protected int penaltyPerCoinUsed;


    @Inject
    public MyFirstProblem(@Constant(value = "numberCoins") int numberCoins, @Constant(value = "targetValue") int targetValue, @Constant(value = "penaltyPerCoinUsed") int penaltyPerCoinUsed) {
        this.numberCoins = numberCoins;
        this.targetValue = targetValue;
        this.penaltyPerCoinUsed = penaltyPerCoinUsed;
    }

    public int getTargetValue() {
        return targetValue;
    }
    public int getNumberCoins() {
        return numberCoins;
    }
    public int getPenaltyPerCoinUsed() {
        return penaltyPerCoinUsed;
    }

}
