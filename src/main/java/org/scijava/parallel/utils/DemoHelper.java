/*******************************************************************************
 * IT4Innovations - National Supercomputing Center
 * Copyright (c) 2017 - 2019 All Right Reserved, https://www.it4i.cz
 *
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this project.
 ******************************************************************************/
package org.scijava.parallel.utils;

import java.util.concurrent.ExecutionException;

import org.scijava.Context;
import org.scijava.command.CommandService;
import org.scijava.parallel.ParallelService;
import org.scijava.parallel.ParallelizationParadigm;

import cz.it4i.command.ParadigmProfilesManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DemoHelper {

	
	private Context context;
	private ParallelService parallelService;
	private CommandService commandService;

	public DemoHelper() {
		context = new Context();
		parallelService = context.getService(ParallelService.class);
		commandService = context.getService(CommandService.class);
	}
	
	public final ParallelizationParadigm getParadigm() {
		ParallelizationParadigm result = parallelService.getParadigm();
		if (result == null) {
			try {
				commandService.run(ParadigmProfilesManager.class, true).get();
			}
			catch (InterruptedException | ExecutionException exc) {
				if (exc instanceof InterruptedException) {
					Thread.currentThread().interrupt();
				}
				log.error(exc.getMessage(), exc);
			}
		}
		return result;
	}
}
