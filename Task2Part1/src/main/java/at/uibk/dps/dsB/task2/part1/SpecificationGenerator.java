package at.uibk.dps.dsB.task2.part1;

import net.sf.opendse.model.*;

/**
 * The {@link SpecificationGenerator} generates the {@link Specification}
 * modeling the orchestration of the customer modeling application discussed in
 * Lecture 1.
 * 
 * @author Fedor Smirnov
 */
public final class SpecificationGenerator {

	private SpecificationGenerator() {
	}

	/**
	 * Generates the specification modeling the orchestration of the customer
	 * monitoring application.
	 * 
	 * @return the specification modeling the orchestration of the customer
	 *         monitoring application
	 */
	public static Specification generate() {
		Application<Task, Dependency> appl = generateApplication();
		Architecture<Resource, Link> arch = generateArchitecture();
		Mappings<Task, Resource> mappings = generateMappings(arch, appl);
		return new Specification(appl, arch, mappings);
	}

	/**
	 * Generates the application graph
	 * 
	 * @return the application graph
	 */
	private static Application<Task, Dependency> generateApplication() {
		var application = new Application<>();
		var t0 = new Task("t0");
		var t1 = new Task("t1");
		var t2 = new Task("t2");
		var t3 = new Task("t3");
		var t4 = new Task("t4");
		var t5 = new Task("t5");

		application.addVertex(t0);
		application.addVertex(t1);
		application.addVertex(t2);
		application.addVertex(t3);
		application.addVertex(t4);
		application.addVertex(t5);

		var c0 = new Communication("c0");
		var c1 = new Communication("c1");
		var c2 = new Communication("c2");
		var c3 = new Communication("c3");
		var c4 = new Communication("c4");

		application.addEdge(new Dependency("d0"), t0, c0);
		application.addEdge(new Dependency("d1"), c0, t1);
		application.addEdge(new Dependency("d2"), t1, c1);
		application.addEdge(new Dependency("d3"), t1, c2);
		application.addEdge(new Dependency("d4"), c1, t2);
		application.addEdge(new Dependency("d5"), c2, t3);
		application.addEdge(new Dependency("d6"), t2, c3);
		application.addEdge(new Dependency("d7"), t3, c4);
		application.addEdge(new Dependency("d8"), c3, t4);
		application.addEdge(new Dependency("d9"), c3, t5);
		application.addEdge(new Dependency("d10"), c4, t4);
		application.addEdge(new Dependency("d11"), c4, t5);



		return application;
	}

	/**
	 * Generates the architecture graph
	 * 
	 * @return the architecture graph
	 */
	private static Architecture<Resource, Link> generateArchitecture() {
		Architecture<Resource, Link> architecture = new Architecture<Resource, Link>();
		var r0 = new Resource("r0");
		var r1 = new Resource("r1");
		var r2 = new Resource("r2");
		var r3 = new Resource("r3");
		var r4 = new Resource("r4");
		var r5 = new Resource("r5");
		var r6 = new Resource("r6");
		var r7 = new Resource("r7");

		architecture.addVertex(r0);
		architecture.addVertex(r1);
		architecture.addVertex(r2);
		architecture.addVertex(r3);
		architecture.addVertex(r4);
		architecture.addVertex(r5);
		architecture.addVertex(r6);
		architecture.addVertex(r7);




		//ETHERNET
		architecture.addEdge(new Link("l0"), r0, r2);
		architecture.addEdge(new Link("l1"), r1, r2);

		//WIFI
		architecture.addEdge(new Link("l2"), r0, r2);
		architecture.addEdge(new Link("l3"), r1, r2);
		architecture.addEdge(new Link("l4"), r3, r2);
		architecture.addEdge(new Link("l5"), r4, r2);
		architecture.addEdge(new Link("l6"), r5, r2);

		//BLUETOOTH
		architecture.addEdge(new Link("l7"), r3, r4);
		architecture.addEdge(new Link("l8"), r3, r5);
		architecture.addEdge(new Link("l9"), r4, r5);

		//INTERNET
		architecture.addEdge(new Link("l10"), r6, r2);
		architecture.addEdge(new Link("l11"), r7, r2);

		return architecture;
	}

	/**
	 * Generates the mapping edges
	 * 
	 * @param arch the architecture graph
	 * @param appl the application graph
	 * @return the mapping edges
	 */
	private static Mappings<Task, Resource> generateMappings(Architecture<Resource, Link> arch,
			Application<Task, Dependency> appl) {
		var mappings = new Mappings<>();

		//The video can be captured by the camera and/or by the smart camera.
		var m0 = new Mapping<Task, Resource>("m0", appl.getVertex("t0"), arch.getVertex("r0"));
		var m1 = new Mapping<Task, Resource>("m1", appl.getVertex("t0"), arch.getVertex("r1"));

		//The pattern recognition can be done in the cloud...
		var m2 = new Mapping<Task, Resource>("m2", appl.getVertex("t1"), arch.getVertex("r6"));
		var m3 = new Mapping<Task, Resource>("m3", appl.getVertex("t1"), arch.getVertex("r7"));

		//on the fog resource (router)...
		var m4 = new Mapping<Task, Resource>("m4", appl.getVertex("t1"), arch.getVertex("r2"));

		//or directly on the smart camera.
		var m5 = new Mapping<Task, Resource>("m5", appl.getVertex("t1"), arch.getVertex("r1"));

		mappings.add(m0);
		mappings.add(m1);
		mappings.add(m2);
		mappings.add(m3);
		mappings.add(m4);
		mappings.add(m5);

		return mappings;
	}

}
