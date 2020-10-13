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
public class MyFirstCreator implements Creator<SelectGenotype<Character>> {
	Character[] ALPHABET = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
			'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', ' ' };
	Random random = new Random();

	@Override
	public SelectGenotype<Character> create() {
		SelectGenotype<Character> genotype = new SelectGenotype<Character>(ALPHABET);
		genotype.init(random, 11);
		return genotype;
	}

}
