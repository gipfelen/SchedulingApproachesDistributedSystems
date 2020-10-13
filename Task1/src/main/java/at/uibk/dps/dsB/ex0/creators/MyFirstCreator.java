package at.uibk.dps.dsB.ex0.creators;

import org.opt4j.core.Genotype;
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

	@Override
	public SelectGenotype<Integer> create() {
		SelectGenotype<Integer> genotype = new SelectGenotype<>(Coins);
		genotype.init(random, 20);
		return genotype;
	}

}
