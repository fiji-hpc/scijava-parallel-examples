package org.scijava.parallel.examples;

import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Command.class)
public class ExampleCommand implements Command{

	@Parameter
	private String interval;

	@Override
	public void run() {
		System.err.println("##############################################");
		System.err.println(interval);
		System.err.println("##############################################");
	}
}
