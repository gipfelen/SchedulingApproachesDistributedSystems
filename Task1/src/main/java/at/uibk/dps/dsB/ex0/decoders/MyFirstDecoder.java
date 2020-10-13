package at.uibk.dps.dsB.ex0.decoders;

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
public class MyFirstDecoder implements Decoder<SelectGenotype<Character>, String> {

	@Override
	public String decode(SelectGenotype<Character> genotype) {
		String phenotype = "";
		for (int i = 0; i < genotype.size(); i++) {
			phenotype += genotype.getValue(i);
		}
		return phenotype;
	}
}
