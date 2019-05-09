package org.scijava.parallel.examples;

import java.util.Collections;

import org.scijava.Context;
import org.scijava.parallel.ParallelizationParadigm;
import org.scijava.parallel.utils.InProcessImageJServerRunner;

import cz.it4i.parallel.TestParadigm;

public class ExampleExecute {

	public static void main(String... args) throws Exception {
		Context context = new Context();
		try (ParallelizationParadigm paradigm = new TestParadigm(
			new InProcessImageJServerRunner(context), context))
		{
			paradigm.runAll(ExampleCommand.class, Collections.singletonList(
				Collections.singletonMap("interval", "Hello World!")));
		}
		System.exit(0);
	}
}
