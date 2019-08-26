
package org.scijava.parallel.examples;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.imagej.ops.math.PrimitiveMath;

import org.scijava.parallel.utils.ExamplesHelper;

import cz.it4i.parallel.RPCParadigm;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AddDoubles
{

	public static void main(String[] args)
	{
		ExamplesHelper demoHelper = new ExamplesHelper();

		try (RPCParadigm paradigm = demoHelper.getParadigm())
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
			log.info("result: " + result);
		}
	}


}
