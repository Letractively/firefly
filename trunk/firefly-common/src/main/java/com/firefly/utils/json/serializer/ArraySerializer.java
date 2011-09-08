package com.firefly.utils.json.serializer;

import static com.firefly.utils.json.JsonStringSymbol.ARRAY_PRE;
import static com.firefly.utils.json.JsonStringSymbol.ARRAY_SUF;
import static com.firefly.utils.json.JsonStringSymbol.SEPARATOR;

import java.io.IOException;
import java.lang.reflect.Array;

import com.firefly.utils.json.Serializer;
import com.firefly.utils.json.support.JsonStringWriter;

public class ArraySerializer implements Serializer {

	@Override
	public void convertTo(JsonStringWriter writer, Object obj) throws IOException {
		writer.append(ARRAY_PRE);
		int len = Array.getLength(obj) - 1;
		if (len > -1) {
			int i;
			for (i = 0; i < len; i++) {
				StateMachine.toJson(Array.get(obj, i), writer);
				writer.append(SEPARATOR);
			}
			StateMachine.toJson(Array.get(obj, i), writer);
		}
		writer.append(ARRAY_SUF);

	}

}