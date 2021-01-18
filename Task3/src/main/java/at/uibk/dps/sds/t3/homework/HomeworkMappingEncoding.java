package at.uibk.dps.sds.t3.homework;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.opendse.encoding.variables.Variable;
import net.sf.opendse.encoding.variables.Variables;
import net.sf.opendse.model.properties.TaskPropertyService;
import org.opt4j.satdecoding.Constraint;

import net.sf.opendse.encoding.mapping.MappingConstraintGenerator;
import net.sf.opendse.encoding.variables.T;
import net.sf.opendse.model.Mappings;
import net.sf.opendse.model.Resource;
import net.sf.opendse.model.Specification;
import net.sf.opendse.model.Task;
import net.sf.opendse.optimization.SpecificationWrapper;
import org.opt4j.satdecoding.Term;

/**
 * 
 * Class for the implementation of the homework.
 * 
 * @author Fedor Smirnov
 */
public class HomeworkMappingEncoding implements MappingConstraintGenerator {
	
	protected final Specification spec;
	
	public HomeworkMappingEncoding(SpecificationWrapper specWrapper) {
		this.spec = specWrapper.getSpecification();
	}

	@Override
	public Set<Constraint> toConstraints(Set<T> processVariables, Mappings<Task, Resource> mappings) {
		var constraintsSecretTasksOnCloudResource = constraintsSecretTasksOnCloudResource(spec);
		var constraintsSecretMessagesDifferentRegions = constraintsSecretMessagesDifferentRegions(spec);
		var constraintsOfEdgeResourceMoreThanTwoTasks = constraintsOfEdgeResourceMoreThanTwoTasks(spec);
		var constraintsOfTasksWithoutResource = constraintsOfTasksWithoutResource(processVariables, spec);

		var constraints = new HashSet<Constraint>();

		constraints.addAll(constraintsSecretTasksOnCloudResource);
		constraints.addAll(constraintsSecretMessagesDifferentRegions);
		constraints.addAll(constraintsOfEdgeResourceMoreThanTwoTasks);
		constraints.addAll(constraintsOfTasksWithoutResource);

		return constraints;
	}

	/**
	 * Returns constrains of cloud resources, that are annotated as a secret
	 * @param implementation the given implementation
	 * @return Set of constrains
	 */
	private Set<Constraint> constraintsSecretTasksOnCloudResource(Specification implementation){
		var constraints = new HashSet<Constraint>();

		Constraint constraint = new Constraint(Constraint.Operator.EQ, 0);
		for(var mapping: implementation.getMappings()){
			if(PropertyService.isCloud(mapping.getTarget()) && PropertyService.isSecret(mapping.getSource())) {
				var mVar = Variables.varM(mapping);
				constraint.add(Variables.p(mVar));
			}
		}
		constraints.add(constraint);

		return constraints;
	}

	/**
	 * Constraints of: "If two tasks exchange messages and are both annotated as secrets, they both must be executed within the same region"
	 * @param implementation
	 * @return Constraints
	 */
	private Set<Constraint> constraintsSecretMessagesDifferentRegions(Specification implementation) {
		var constraints = new HashSet<Constraint>();

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

			var mappingsSender = implementation.getMappings().get(task);
			var regionsSender = getRegions(task, implementation.getMappings());

			for(Task receiver: receivingTasks) {

				// get regions for resources of receiving task
				var mappingsReceiver = implementation.getMappings().get(receiver);
				List<String> regionsReceiver = getRegions(receiver, implementation.getMappings());

				Constraint constraint = new Constraint(Constraint.Operator.EQ, 0);

				for(var mapping: mappingsSender){
					if(!regionsReceiver.contains(PropertyService.getRegion(mapping.getTarget()))){
						constraint.add(Variables.p(Variables.varM(mapping)));
					}
				}

				for(var mapping: mappingsReceiver){
					if(!regionsSender.contains(PropertyService.getRegion(mapping.getTarget()))){
						constraint.add(Variables.p(Variables.varM(mapping)));
					}
				}

				constraints.add(constraint);
			}

		}

		return constraints;
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
	 * Constraints of: "Due to their restricted capacity, at most 2 tasks can be executed on a single edge resource"
	 * @param implementation
	 * @return Constraints
	 */
	private Set<Constraint> constraintsOfEdgeResourceMoreThanTwoTasks(Specification implementation){

		var constraints = new HashSet<Constraint>();

		var edgeResources = new ArrayList<Resource>();

		for(var mapping: implementation.getMappings()){
			var resource = mapping.getTarget();

			if (PropertyService.isEdge(resource)){
				edgeResources.add(resource);
			}
		}

		for (Resource res : edgeResources) {
			Constraint constraint = new Constraint(Constraint.Operator.LE, 2);
			for(var mapping: implementation.getMappings().get(res)) {
				var mVar = Variables.varM(mapping);
				constraint.add(Variables.p(mVar));
			}
			constraints.add(constraint);

		}

		return constraints;
	}

	/**
	 * Constraints of: "Each task has to be mapped onto at least one resource"
	 * @param implementation
	 * @return Constraints
	 */
	private Set<Constraint> constraintsOfTasksWithoutResource(Set<T> processVariables, Specification implementation){

		var constraints = new HashSet<Constraint>();

		for (var task : processVariables) {
			var taskMappings = implementation.getMappings().get(task.getTask());

			Constraint constraint = new Constraint(Constraint.Operator.GE, 0);
			constraint.add(new Term(-1, Variables.p(task))); 	// Here you have to pay attention to use the Variables from the encoding
																	// project, not from the optimization project

			for(var mapping: taskMappings){
				var mVar = Variables.varM(mapping);
				constraint.add(Variables.p(mVar));
			}

			constraints.add(constraint);
		}

		return constraints;
	}
}
