package at.uibk.dps.sds.t3.homework;

import net.sf.opendse.model.Mappings;
import net.sf.opendse.model.Resource;
import net.sf.opendse.model.Task;
import net.sf.opendse.model.properties.TaskPropertyService;
import org.opt4j.core.Objective;
import org.opt4j.core.Objectives;
import org.opt4j.core.Objective.Sign;

import net.sf.opendse.model.Specification;
import net.sf.opendse.optimization.ImplementationEvaluator;

import javax.swing.plaf.synth.Region;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * The evaluator used to enforce the security constraints by means of additional
 * objectives.
 * 
 * @author Fedor Smirnov
 *
 */
public class HwConstraintEvaluator implements ImplementationEvaluator {

	protected final Objective numConstraintViolations = new Objective("Num Constraint Violations", Sign.MIN);

	@Override
	public Specification evaluate(Specification implementation, Objectives objectives) {
		objectives.add(numConstraintViolations, countConstraintViolations(implementation));
		return null;
	}

	/**
	 * Counts the number of constraint violations in the given implementation
	 * 
	 * @param implementation the given implementation
	 * @return the number of constraint violations
	 */
	protected int countConstraintViolations(Specification implementation) {
		var numberSecretTasksOnCloudResource = numberSecretTasksOnCloudResource(implementation);
		var numberSecretMessagesDifferentRegions = numberSecretMessagesDifferentRegions(implementation);
		var numberOfEdgeResourceMoreThanTwoTasks = numberOfEdgeResourceMoreThanTwoTasks(implementation);
		var numberOfTasksWithoutResource = numberOfTasksWithoutResource(implementation);
		return numberSecretTasksOnCloudResource + numberSecretMessagesDifferentRegions
				+ numberOfEdgeResourceMoreThanTwoTasks + numberOfTasksWithoutResource;
	}

	/**
	 * Counts the number of cloud resources, that are annotated as a secret
	 * @param implementation the given implementation
	 * @return the number of constraint violations
	 */
	private int numberSecretTasksOnCloudResource(Specification implementation){
		var numberViolations = 0;

		for(var mapping: implementation.getMappings()){
			if(PropertyService.isCloud(mapping.getTarget()) && PropertyService.isSecret(mapping.getSource())) {
				numberViolations++;
			}
		}

		return numberViolations;
	}

	/**
	 * Number of violations due to "If two tasks exchange messages and are both annotated as secrets, they both must be executed within the same region"
	 * @param implementation
	 * @return Number of violations
	 */
	private int numberSecretMessagesDifferentRegions(Specification implementation) {
		var numberViolations = 0;

		for (var task : implementation.getApplication().getVertices()) {

			//Check if communication and secret
			if(!TaskPropertyService.isCommunication(task) || !PropertyService.isSecret(task)){
				continue;
			}

			//Getting task receiving from current task
			var receivingTasks = new ArrayList<Task>();
			for(var mappings: implementation.getApplication().getOutEdges(task)){
				var dest = implementation.getApplication().getDest(mappings);

				if(PropertyService.isSecret(dest)){
					receivingTasks.add(dest);
				}
			}

			var regionsSender = getRegions(task, implementation.getMappings());

			for(Task receiver: receivingTasks) {

				// get regions for resources of receiving task
				List<String> regionsReceiver = getRegions(receiver, implementation.getMappings());

				// check if sender and receiver have exactly the same regions
				if(regionsReceiver.size() != regionsSender.size() || !regionsReceiver.containsAll(regionsSender)) {
					numberViolations++;
				}
			}

		}

		return numberViolations;
	}

	/**
	 * Returns all regions for a given task
	 * @param t Task to get the regions from
	 * @param mappings Mapping of all tasks
	 * @return All regions of t.
	 */
	private List<String> getRegions(Task t, Mappings<Task, Resource> mappings) {
		var regionList = new ArrayList<String>();
		for(var mapping: mappings.get(t)){
			var region = PropertyService.getRegion(mapping.getTarget());
			if (!regionList.contains(region)){
				regionList.add(region);
			}
		}

		return regionList;
	}

	/**
	 * Number of violations due to: "Due to their restricted capacity, at most 2 tasks can be executed on a single edge resource"
	 * @param implementation
	 * @return Number of violations
	 */
	private int numberOfEdgeResourceMoreThanTwoTasks(Specification implementation){

		var numberViolations = 0;

		var edgeResources = new ArrayList<Resource>();

		for(var mapping: implementation.getMappings()){
			var resource = mapping.getTarget();

			if (PropertyService.isEdge(resource)){
				edgeResources.add(resource);
			}
		}

		for (Resource res : edgeResources) {
			if(implementation.getMappings().get(res).size() > 2) {
				numberViolations++;
			}
		}

		return numberViolations;
	}

	/**
	 * Number of violations due to: "Each task has to be mapped onto at least one resource"
	 * @param implementation
	 * @return Number of violations
	 */
	private int numberOfTasksWithoutResource(Specification implementation){

		var numberViolations = 0;

		var tasks = implementation.getApplication().getVertices();

		for (var task : tasks) {
			if (TaskPropertyService.isCommunication(task)) {
				continue;
			}
			var taskMappings = implementation.getMappings().get(task);

			if (taskMappings == null || taskMappings.size() == 0) {
				numberViolations++;
			}
		}

		return numberViolations;
	}

	@Override
	public int getPriority() {
		// independent of other stuff
		return 0;
	}
}
