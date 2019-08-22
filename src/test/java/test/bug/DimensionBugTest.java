
package test.bug;

import static org.junit.Assert.assertArrayEquals;

import java.util.Collections;
import java.util.HashMap;

import net.imagej.Dataset;
import net.imagej.DefaultDataset;
import net.imagej.ImgPlus;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.ImgView;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.util.Intervals;
import net.imglib2.view.Views;

import org.junit.Test;
import org.scijava.Context;
import org.scijava.ItemIO;
import org.scijava.command.Command;
import org.scijava.parallel.ParallelizationParadigm;
import org.scijava.parallel.utils.ExamplesHelper;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

public class DimensionBugTest {

	private static final long[] IMAGE_DIMENSIONS = { 100, 100, 1 };

	@Test
	public void testDimenstionInDataset() {
		ExamplesHelper demoHelper = new ExamplesHelper();
		try (final ParallelizationParadigm paradigm = demoHelper.getParadigm()) {
			paradigm.init();
			Dataset result = (Dataset) paradigm.runAll(DimensionBugDemo.class,
				Collections.singletonList(new HashMap<>())).get(0).get("output");

			long[] dimensions = Intervals.dimensionsAsLongArray(result);

			assertArrayEquals(IMAGE_DIMENSIONS, dimensions);
		}
	}

	@Plugin(type = Command.class)
	public static class DimensionBugDemo implements Command {

		@Parameter(type = ItemIO.OUTPUT)
		private Dataset output;

		@Parameter
		private Context context;

		@Override
		public void run() {
			output = wrapAsDataset(context, ArrayImgs.unsignedBytes(
				IMAGE_DIMENSIONS));
		}

		private static Dataset wrapAsDataset(Context context,
			RandomAccessibleInterval<UnsignedByteType> output)
		{
			final ImgPlus<UnsignedByteType> imgPlus = new ImgPlus<>(ImgView.wrap(Views
				.zeroMin(output), null));
			Dataset example = new DefaultDataset(context, imgPlus);
			return example;
		}

		// TODO: Support RandomAccessibleInterval, Img, ImgPlus.
		// TODO: Don't require to specify "dummy.png" when transferring Datasets.
		// TODO: TIFF must be supported as image transfer format.
		// TODO: Better error handling, getting multiple print outs of the same
		// error
		// message is very annoying.
	}
}
