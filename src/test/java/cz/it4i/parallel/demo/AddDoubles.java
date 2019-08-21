
package cz.it4i.parallel.demo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.imagej.ops.math.PrimitiveMath;

import org.scijava.parallel.ParallelizationParadigm;
import org.scijava.parallel.utils.DemoHelper;

public class AddDoubles
{

	public static void main(String[] args)
	{
		DemoHelper demoHelper = new DemoHelper();

		try (ParallelizationParadigm paradigm = demoHelper.getParadigm())
		{
			paradigm.init();
			List<Map<String, Object>> inputs = new LinkedList<>();
			for (int i = 0; i < 100; i++) {
				Map<String, Object> params = new HashMap<>();
				params.put("a", 10 * i);
				params.put("b", 20 * i);
				inputs.add(params);
			}

			List<Map<String, Object>> result = paradigm.runAll(
				PrimitiveMath.DoubleMultiply.class, inputs);
			System.out.println("result: " + result);
		}
	}


}
