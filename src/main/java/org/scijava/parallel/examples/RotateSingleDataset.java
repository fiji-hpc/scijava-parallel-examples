
package org.scijava.parallel.examples;

import io.scif.services.DatasetIOService;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.imagej.Dataset;
import net.imagej.plugins.commands.imglib.RotateImageXY;

import org.scijava.parallel.ParallelizationParadigm;
import org.scijava.parallel.utils.ExamplesHelper;

import cz.it4i.parallel.SciJavaParallelRuntimeException;

import org.scijava.parallel.utils.ExampleImage;

/**
 * Demonstration example showing basic usage of ParalellizationParadigm. It
 * downloads a picture (Lena) and rotate it for 90 degree. Result is immediately
 * showed.
 * 
 * @author koz01
 */
public class RotateSingleDataset
{

	public static void main(String[] args)
	{
		ExamplesHelper demoHelper = new ExamplesHelper();
		try (ParallelizationParadigm paradigm = demoHelper.getParadigm())
		{
			paradigm.init();
			demoHelper.getUiService().show(rotateSingleDataset(demoHelper
				.getDatasetIOService(), paradigm));
		}
	}

	static Object rotateSingleDataset( DatasetIOService ioService, ParallelizationParadigm paradigm )
	{
		List< Map< String, Object > > parametersList = initParameters(ioService);
		List<Map<String, Object>> results = paradigm.runAll(RotateImageXY.class,
				parametersList);
		return results.get( 0 ).get( "dataset" );
	}

	private static List< Map< String, Object > > initParameters( DatasetIOService ioService )
	{
		try
		{
			Dataset dataset = ioService.open( ExampleImage.lenaAsTempFile().toString());
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("dataset", dataset);
			parameters.put("angle", 90);
			return Collections.singletonList(parameters);
		}
		catch ( IOException e )
		{
			throw new SciJavaParallelRuntimeException(e);
		}
	}
}
