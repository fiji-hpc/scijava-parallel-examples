/*******************************************************************************
 * IT4Innovations - National Supercomputing Center
 * Copyright (c) 2017 - 2019 All Right Reserved, https://www.it4i.cz
 *
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE', which is part of this project.
 ******************************************************************************/
package org.scijava.parallel.utils;

import io.scif.services.DatasetIOService;

import java.util.concurrent.ExecutionException;

import org.scijava.Context;
import org.scijava.command.CommandService;
import org.scijava.parallel.ParallelService;
import org.scijava.parallel.ParallelizationParadigm;
import org.scijava.ui.UIService;

import cz.it4i.command.ParadigmProfilesManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DemoHelper {

	
	private final Context context;
	private final ParallelService parallelService;
	private final CommandService commandService;
	private UIService uiService;
	private DatasetIOService datasetIOService;

	public DemoHelper() {
		context = new Context();
		parallelService = context.getService(ParallelService.class);
		commandService = context.getService(CommandService.class);
		uiService = context.getService(UIService.class);
		datasetIOService = context.getService(DatasetIOService.class);
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

	public Context getContext() {
		return context;
	}

	public DatasetIOService getDatasetIOService() {
		return datasetIOService;
	}

	public UIService getUiService() {
		return uiService;
	}
}
