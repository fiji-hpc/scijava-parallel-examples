
package cz.it4i.parallel.demo;

import io.scif.services.DatasetIOService;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.imagej.Dataset;
import net.imagej.plugins.commands.imglib.RotateImageXY;

import org.scijava.Context;
import org.scijava.parallel.ParallelizationParadigm;
import org.scijava.parallel.utils.TestParadigm;
import org.scijava.ui.UIService;

/**
 * Demonstration example showing basic usage of ParalellizationParadigm with
 * ImageJ server started in local system. It downloads a picture (Lena) and
 * rotate it for 90 degree. Result is immediately showed.
 * 
 * @author koz01
 */
public class RotateSingleDataset
{

	public static void main(String[] args)
	{
		Context context = new Context();
		DatasetIOService ioService = context.service( DatasetIOService.class );
		UIService uiService = context.service( UIService.class );
		try( ParallelizationParadigm paradigm = TestParadigm.localImageJServer( Config.getFijiExecutable(), context ))
		{
			uiService.show( rotateSingleDataset( ioService, paradigm ) );
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
			throw new RuntimeException( e );
		}
	}
}
