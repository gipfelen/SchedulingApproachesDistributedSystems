package at.uibk.dps.dsB.ex0.creators;

import at.uibk.dps.dsB.ex0.MyFirstProblem;
import com.google.inject.Inject;
import org.opt4j.core.genotype.SelectGenotype;
import org.opt4j.core.problem.Creator;

import java.util.Random;

/**
 * The {@link Creator} class which will be used to initialize the genotypes
 * encoding individual problem solutions.
 * 
 * @author Fedor Smirnov
 *
 */
public class MyFirstCreator implements Creator<SelectGenotype<Integer>> {

	Integer[] Coins = { 200, 100, 50, 20, 10, 5, 2, 1, 0};
	Random random = new Random();

	protected final MyFirstProblem problem;
	@Inject
	public MyFirstCreator(MyFirstProblem problem) {
		this.problem = problem;
	}

	@Override
	public SelectGenotype<Integer> create() {
		SelectGenotype<Integer> genotype = new SelectGenotype<>(Coins);
		genotype.init(random, problem.getNumberCoins());
		return genotype;
	}

}
