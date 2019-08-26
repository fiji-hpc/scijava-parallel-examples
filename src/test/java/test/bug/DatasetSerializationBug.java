package test.bug;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.imagej.Dataset;
import net.imagej.DatasetService;
import net.imglib2.img.array.ArrayImgs;

import org.junit.Test;
import org.scijava.Context;
import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.parallel.utils.ExamplesHelper;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import cz.it4i.parallel.RPCParadigm;

public class DatasetSerializationBug
{
	private final Context context = new Context();
	private final DatasetService datasetService = context.service( DatasetService.class );

	@Test
	public void workingOneData()
	{
		ExamplesHelper demoHelper = new ExamplesHelper();
		try (RPCParadigm paradigm = demoHelper.getParadigm())
		{
			paradigm.init();
			paradigm.runAll( TestCommand.class, Collections.singletonList( initParameters( "dummy1.png" ) ) );
		}
	}

	@Test
	public void workingTwoDataset()
	{
		ExamplesHelper demoHelper = new ExamplesHelper();
		try (RPCParadigm paradigm = demoHelper.getParadigm())
		{
			paradigm.init();
			paradigm.runAll( TestCommand.class, Arrays.asList(
					initParameters( "dummy1.png" ),
					initParameters( "dummy2.png" )
			) );
		}
	}

	private Map< String, Object > initParameters( String name )
	{
		Map<String, Object> parameters = new HashMap<>();
		Dataset dataset = datasetService.create( ArrayImgs.unsignedBytes( 10, 10 ));
		dataset.setName(name);
		parameters.put("image", dataset);
		return parameters;
	}

	@Plugin( type = Command.class )
	public static class TestCommand implements Command
	{
		@Parameter(type = ItemIO.BOTH)
		Dataset image;

		@Override
		public void run()
		{
			image.forEach( pixel -> pixel.setOne() );
		}
	}
}
