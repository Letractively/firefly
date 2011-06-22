package test.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import static org.hamcrest.Matchers.*;
import org.junit.Assert;
import org.junit.Test;
import com.firefly.utils.ConvertUtils;
import com.firefly.utils.log.Log;
import com.firefly.utils.log.LogFactory;

public class TestConvertUtils {
	private static Log log = LogFactory.getInstance().getLog("firefly-system");

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testConvertArray() throws Exception {
		Collection collection = new ArrayList();
		collection.add("arr1");
		collection.add("arr2");
		Method method = TestConvertUtils.class.getMethod("setArray",
				String[].class);
		Object obj = ConvertUtils.convert(collection,
				method.getParameterTypes()[0]);
		Integer ret = (Integer)method.invoke(this, obj);
		Assert.assertThat(ret, is(2));
	}

	@Test
	public void testAutoConvertLong() {
		Long x = ConvertUtils.convert("10000000000", "");
		Assert.assertThat(x, is(10000000000L));
	}

	public int setArray(String[] arr) {
		log.debug(Arrays.toString(arr));
		return arr.length;
	}

}
