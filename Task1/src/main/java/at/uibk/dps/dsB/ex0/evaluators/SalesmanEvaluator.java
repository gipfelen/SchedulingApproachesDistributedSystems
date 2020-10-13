package at.uibk.dps.dsB.ex0.evaluators;

import at.uibk.dps.dsB.ex0.SalesmanProblem;
import at.uibk.dps.dsB.ex0.SalesmanRoute;
import org.opt4j.core.Objective;
import org.opt4j.core.Objectives;
import org.opt4j.core.problem.Evaluator;

public class SalesmanEvaluator implements Evaluator<SalesmanRoute> {
    public Objectives evaluate(SalesmanRoute salesmanRoute) {
        double dist = 0;
        for (int i = 0; i < salesmanRoute.size(); i++) {
            SalesmanProblem.City one = salesmanRoute.get(i);
            SalesmanProblem.City two = salesmanRoute.get((i + 1) % salesmanRoute.size());
            dist += getEuclideanDistance(one, two);
        }
        Objectives objectives = new Objectives();
        objectives.add("distance", Objective.Sign.MIN, dist);
        return objectives;
    }
    private double getEuclideanDistance(SalesmanProblem.City one, SalesmanProblem.City two) {
        final double x = one.getX() - two.getX();
        final double y = one.getY() - two.getY();
        return Math.sqrt(x * x + y * y);
    }
}