package at.uibk.dps.dsB.task2.part2.evaluation;

import at.uibk.dps.dsB.task2.part2.properties.PropertyService;
import net.sf.opendse.model.*;
import net.sf.opendse.model.properties.TaskPropertyService;
import org.opt4j.core.Objective;
import org.opt4j.core.Objective.Sign;
import org.opt4j.core.Objectives;

import at.uibk.dps.dsB.task2.part2.properties.PropertyProvider;
import at.uibk.dps.dsB.task2.part2.properties.PropertyProviderStatic;
import net.sf.opendse.model.Specification;
import net.sf.opendse.optimization.ImplementationEvaluator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Evaluator for the makespan of the Piw3000
 * 
 * @author Fedor Smirnov
 */
public class TimingEvaluator implements ImplementationEvaluator {

	protected final PropertyProvider propertyProvider = new PropertyProviderStatic();

	protected static final int priority = 0;

	protected final Objective makeSpanObjective = new Objective("Makespan [TU]", Sign.MIN);

	protected final String endTimeAttribute = "End Time";
	public static final String accumulatedUsageAttribute = "Accumulated Usage";

	@Override
	public Specification evaluate(Specification implementation, Objectives objectives) {
		objectives.add(makeSpanObjective, calculateMakespan(implementation));
		// Implementation annotated => return the impl
		return implementation;
	}

	/**
	 * Does the actual makespan calculation.
	 * 
	 * @param implementation the orchestration under evaluation
	 * @return the makespan of the orchestration
	 */
	protected double calculateMakespan(Specification implementation) {
		var appl = implementation.getApplication();
		var root = getRoot(appl);
		if(root == null){
			return 0;
		}

		//Saves the start/end times for each task
		Map<Task, Double> startTimes = new HashMap<>();
		Map<Task, Double> endTimes = new HashMap<>();

		startTimes.put(root, 0d);
		endTimes = calculateEndTime(implementation,root,startTimes,endTimes);

		double maxEndingTime = 0d;
		for(var endTime: endTimes.entrySet()){
			maxEndingTime = Math.max(maxEndingTime,endTime.getValue());
		}

		return maxEndingTime;
	}

	private Task getRoot(Application<Task, Dependency> appl){
		for (Task task : appl) {
			if (appl.getPredecessorCount(task) == 0) {
				return task;
			}
		}
		return null;
	}

	private int getNumberOfInstances(Task t) {
		var type = t.getAttribute("TYPE");

		if(type == "ITERATIVE_CARS") {
			// number of execution = number of cars
			return propertyProvider.getCarNumber();
		} else if(type == "ITERATIVE_PEOPLE") {
			// number of execution = number of people
			return propertyProvider.getNumberOfPeople();
		}

		return 1;
	}

	private double getTaskTime(Task task, Specification implementation) {
		var resourceMappings = implementation.getMappings();
		var taskResources = resourceMappings.get(task);

		// You can assume that exactly one resource type is chosen for every task
		var taskResource = taskResources.iterator().next();
		double executionTime = this.propertyProvider.getExecutionTime(taskResource);

		int numberOfInstances = this.getNumberOfInstances(task);
		var resource = taskResource.getTarget();

		double usage = executionTime * numberOfInstances;
		if (resource.getAttribute(accumulatedUsageAttribute) != null) {
			usage = usage + (double) resource.getAttribute(accumulatedUsageAttribute);

		}
		resource.setAttribute(accumulatedUsageAttribute, usage);

		if (PropertyService.isSingleExecTask(task)) {
			return executionTime;
		}


		var availableResources = 1;
		// If we have multiple resources
		if (PropertyService.isCloudResource(resource)) {
			availableResources = this.propertyProvider.getNumberOfAvailableInstances(resource);
		}

		// How many runs are possible?
		long runs = (long) Math.ceil((double) numberOfInstances / (double) availableResources);
		return executionTime * runs;
	}

	private Map<Task, Double> calculateEndTime(Specification implementation, Task currentTask, Map<Task, Double> startTimes,
											   Map<Task, Double> endTimes){
		var appl = implementation.getApplication();
		var timeCurrentTask = 0d;



		if (TaskPropertyService.isCommunication(currentTask)){
			var transmissionTime = 0d;
			for (var link: implementation.getRoutings().get(currentTask).getEdges()){
				transmissionTime += propertyProvider.getTransmissionTime((Communication) currentTask,link);
			}

			timeCurrentTask = transmissionTime * getNumberOfInstances(currentTask);
		} else if(TaskPropertyService.isProcess(currentTask)){
			timeCurrentTask = getTaskTime(currentTask, implementation);
		}

		double endTime = startTimes.get(currentTask) + timeCurrentTask;
		endTimes.put(currentTask, endTime);

		currentTask.setAttribute("End time", endTime);
		
		// Set the end time as the start time for all successors
		for (Task successor : appl.getSuccessors(currentTask)) {
			
			// FEDOR: missing check whether the end times of all predecessors of the task have already been calculated 
			startTimes.put(successor, endTime);	
			successor.setAttribute("Start time", endTime);
			endTimes = calculateEndTime(implementation, successor, startTimes, endTimes);
		}

		return endTimes;
	}

	@Override
	public int getPriority() {
		return priority;
	}
}
