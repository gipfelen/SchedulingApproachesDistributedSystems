package at.uibk.dps.dsB.ex0.decoders;

import edu.uci.ics.jung.graph.util.Pair;
import org.opt4j.core.Genotype;
import org.opt4j.core.genotype.SelectGenotype;
import org.opt4j.core.problem.Decoder;

/**
 * The {@link Decoder} class which will be used to decode the genotypes, i.e.,
 * transform them into a representation which can be processed by the evaluator.
 * 
 * @author Fedor Smirnov
 *
 */
public class MyFirstDecoder implements Decoder<SelectGenotype<Integer>, Pair<Integer>> {

	@Override
	public Pair<Integer> decode(SelectGenotype<Integer> genotype) {
		Integer total = 0;
		Integer numberCoins = 0;
		for (int i = 0; i < genotype.size(); i++) {
			total += genotype.getValue(i);
			if (genotype.getValue(i) != 0) {
				numberCoins++;
			}
		}
		return new Pair<>(total,numberCoins);
	}
}
