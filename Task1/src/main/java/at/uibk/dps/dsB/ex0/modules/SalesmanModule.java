package at.uibk.dps.dsB.ex0.modules;

import at.uibk.dps.dsB.ex0.creators.SalesmanCreator;
import at.uibk.dps.dsB.ex0.decoders.SalesmanDecoder;
import at.uibk.dps.dsB.ex0.evaluators.SalesmanEvaluator;
import org.opt4j.core.problem.ProblemModule;
import org.opt4j.core.start.Constant;

public class SalesmanModule extends ProblemModule {
    @Constant(value = "size")
    protected int size = 100;
    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }
    public void config() {
        bindProblem(SalesmanCreator.class, SalesmanDecoder.class, SalesmanEvaluator.class);
    }
}