package at.uibk.dps.dsB.ex0.evaluators;

import at.uibk.dps.dsB.ex0.MyFirstProblem;
import com.google.inject.Inject;
import edu.uci.ics.jung.graph.util.Pair;
import org.opt4j.core.Objective;
import org.opt4j.core.Objective.Sign;
import org.opt4j.core.Objectives;
import org.opt4j.core.problem.Decoder;
import org.opt4j.core.problem.Evaluator;

/**
 * The {@link Evaluator} class which will be used to evaluate the phenotypes
 * returned by the {@link Decoder}.
 * 
 * @author Fedor Smirnov
 *
 */
public class MyFirstEvaluator implements Evaluator<Pair<Integer>> {

	protected final Objective myObjective = new Objective("Objective to maximize", Sign.MIN);

	protected final MyFirstProblem problem;
	@Inject
	public MyFirstEvaluator(MyFirstProblem problem) {
		this.problem = problem;
	}

	@Override
	public Objectives evaluate(Pair<Integer> value) {
		var totalValue = value.getFirst();
		var dif = totalValue - problem.getTargetValue();
		dif = dif > 0 ? dif : - dif;

		//penalty for more coins
		dif += value.getSecond() * problem.getPenaltyPerCoinUsed();
		Objectives objectives = new Objectives();
		objectives.add("objective", Sign.MIN, dif);
		return objectives;
	}

	/**
	 * Calculates the fitness (bigger fitness -> better solution) of the given
	 * phenotype.
	 * 
	 * @param phenotype the given phenotype
	 * @return the fitness of the given phenotype
	 */
	protected double calculatePhenotypeFitness(Object phenotype) {
		// TODO Implement the fitness calculation for your problem
		throw new IllegalArgumentException("Fitness calculation not yet implemented.");
	}
}
