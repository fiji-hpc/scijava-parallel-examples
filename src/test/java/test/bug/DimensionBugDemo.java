package test.bug;

import static org.junit.Assert.assertArrayEquals;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.imagej.Dataset;
import net.imagej.DefaultDataset;
import net.imagej.ImgPlus;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.img.ImgView;
import net.imglib2.img.array.ArrayImgs;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.util.Intervals;
import net.imglib2.view.Views;

import org.scijava.Context;
import org.scijava.command.Command;
import org.scijava.parallel.ParallelizationParadigm;
import org.scijava.parallel.utils.InProcessImageJServerRunner;
import org.scijava.parallel.utils.TestParadigm;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

@Plugin(type = Command.class)
public class DimensionBugDemo implements Command {

	private static final long[] IMAGE_DIMENSIONS = { 100, 100, 1 };

	@Parameter
	private Dataset input;

	@Override
	public void run() {
		long[] dimensions = Intervals.dimensionsAsLongArray(input);
		assertArrayEquals(IMAGE_DIMENSIONS, dimensions);
	}

	public static void main(String... args) {
		final Context context = new Context();
		final InProcessImageJServerRunner runner = new InProcessImageJServerRunner(
			context);
		try (final ParallelizationParadigm paradigm = new TestParadigm(runner,
			context))
		{
			Map<String, Object> map = new HashMap<>();
			map.put("input", wrapAsDataset(context, ArrayImgs.unsignedBytes(
				IMAGE_DIMENSIONS)));
			List<Map<String, Object>> parameters = Collections.singletonList(map);
			paradigm.runAll(DimensionBugDemo.class, parameters);
		}
	}

	private static Dataset wrapAsDataset(Context context,
		RandomAccessibleInterval<UnsignedByteType> output)
	{
		final ImgPlus<UnsignedByteType> imgPlus = new ImgPlus<>(ImgView.wrap(Views
			.zeroMin(output), null));
		Dataset example = new DefaultDataset(context, imgPlus);
		example.setName("dummy.png");
		return example;
	}

	// TODO: Support RandomAccessibleInterval, Img, ImgPlus.
	// TODO: Don't require to specify "dummy.png" when transferring Datasets.
	// TODO: TIFF must be supported as image transfer format.
	// TODO: Better error handling, getting multiple print outs of the same error
	// message is very annoying.
}

