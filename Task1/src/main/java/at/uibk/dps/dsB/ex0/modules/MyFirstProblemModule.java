package at.uibk.dps.dsB.ex0.modules;

import org.opt4j.core.problem.ProblemModule;

import at.uibk.dps.dsB.ex0.creators.MyFirstCreator;
import at.uibk.dps.dsB.ex0.decoders.MyFirstDecoder;
import at.uibk.dps.dsB.ex0.evaluators.MyFirstEvaluator;
import org.opt4j.core.start.Constant;

/**
 * In general, Opt4J's modules are use to configure the binding of interfaces to
 * concrete classes. In this case, we are binding the Creator, Decoder, and
 * Evaluator interfaces to the classes defined throughout the Exercise0 project.
 * 
 * You don't have to alter anything within this class.
 * 
 * @author Fedor Smirnov
 */
public class MyFirstProblemModule extends ProblemModule {

	@Constant(value = "numberCoins")
	protected int numberCoins = 20;

	@Constant(value = "targetValue")
	protected int targetValue = 100;

	@Constant(value = "penaltyPerCoinUsed")
	protected int penaltyPerCoinUsed = 1;


	public int getTargetValue() {
		return targetValue;
	}
	public void setTargetValue(int targetValue) {
		this.targetValue = targetValue;
	}

	public int getNumberCoins() {
		return numberCoins;
	}
	public void setNumberCoins(int numberCoins) {
		this.numberCoins = numberCoins;
	}

	public int getPenaltyPerCoinUsed() {
		return penaltyPerCoinUsed;
	}
	public void setPenaltyPerCoinUsed(int penaltyPerCoinUsed) {
		this.penaltyPerCoinUsed = penaltyPerCoinUsed;
	}


	@Override
	protected void config() {
		bindProblem(MyFirstCreator.class, MyFirstDecoder.class, MyFirstEvaluator.class);
	}
}
