package com.github.homeant.validator.core.util;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

/**
 * MapUtils
 *
 * @author junchen
 * @date 2019-12-29 10:12
 */
public class MapUtils {

	/**
	 * 把一个多层的map变为一层
	 *
	 * @param source
	 * @param path
	 */
	public static Map<String, Object> flattenedMap(Map<String, Object> source, String path) {
		Map<String, Object> result = Maps.newLinkedHashMap();
		source.forEach((key, value) -> {
			if (StringUtils.isNotBlank(path)) {
				if (key.startsWith("[")) {
					key = path + key;
				} else {
					key = path + '.' + key;
				}
			}
			if (value instanceof String) {
				result.put(key, value);
			} else if (value instanceof Map) {
				Map<String, Object> map = (Map) value;
				result.putAll(MapUtils.flattenedMap(map, key));
			} else if (value instanceof Collection) {
				Collection<Object> collection = (Collection) value;
				if (collection.isEmpty()) {
					result.put(key, "");
				} else {
					int count = 0;
					Iterator var7 = collection.iterator();

					while (var7.hasNext()) {
						Object object = var7.next();
						result.putAll(MapUtils.flattenedMap(Collections.singletonMap("[" + count++ + "]", object), key));
					}
				}
			} else {
				result.put(key, value != null ? value : "");
			}
		});
		return result;
	}
}
