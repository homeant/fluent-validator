package com.github.homeant.validator.core.spring;

import com.github.homeant.validator.core.domain.EntityRule;
import com.github.homeant.validator.core.domain.PropertySpec;
import com.github.homeant.validator.core.util.MapUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.core.io.Resource;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * ValidatorBeanFactory
 *
 * @author junchen
 * @date 2019-12-28 23:52
 */
@RequiredArgsConstructor
public class ValidatorSpecContext {

	private static final Map<String, EntityRule> rule_cache = Maps.newConcurrentMap();

	private static final Map<String, Map<String, String>> property_cache = Maps.newConcurrentMap();

	private final List<Resource> resourcesList;

	public void init() throws IOException {
		if (CollectionUtils.isNotEmpty(resourcesList)) {
			for (Resource resource : resourcesList) {
				List<Map<String, Object>> resultList = Lists.newLinkedList();
				LoaderOptions options = new LoaderOptions();
				options.setAllowDuplicateKeys(false);
				try (InputStream inputStream = resource.getInputStream()) {
					Iterator<Object> iterator = new Yaml(options).loadAll(inputStream).iterator();
					while (iterator.hasNext()) {
						Object objectResult = iterator.next();
						Map<String, Object> map = this.asMap(objectResult);
						if (map.containsKey("document")) {
							List<Map> list = (List) map.get("document");
							list.stream().forEach(r -> resultList.add(r));
						} else {
							resultList.add(map);
						}
					}
					resultList.stream().forEach(r -> {
						EntityRule rule = new EntityRule();
						BeanMap beanMap = BeanMap.create(rule);
						beanMap.putAll(r);
						rule_cache.put(rule.getActualType(), rule);
						if (r.containsKey("property")) {
							Map property = (Map) r.get("property");
							property_cache.put(rule.getActualType(), MapUtils.flattenedMap(property, null));
						}
					});
				}
			}
		}
	}

	public EntityRule get(@NonNull Class clazz) {
		return rule_cache.get(clazz.getCanonicalName());
	}

	public Map<String, String> getPropertySpec(@NonNull Class clazz) {
		if (property_cache.containsKey(clazz.getCanonicalName())) {
			return property_cache.get(clazz.getCanonicalName());
		}
		return Maps.newHashMap();
	}

	public void put(@NonNull EntityRule rule) {
		if (StringUtils.isNotBlank(rule.getActualType())) {
			rule_cache.put(rule.getActualType(), rule);
			if (rule.getProperty() != null) {
				property_cache.put(rule.getActualType(), MapUtils.flattenedMap((Map) rule.getProperty(), null));
			}
		}
	}

	public Boolean has(@NonNull Class clazz) {
		return rule_cache.containsKey(clazz.getCanonicalName());
	}

	private Map<String, Object> asMap(Object object) {
		Map<String, Object> resultMap = new LinkedHashMap();
		if (!(object instanceof Map)) {
			resultMap.put("document", object);
		} else if (object instanceof Map) {
			Map<Object, Object> map = (Map) object;
			map.forEach((key, value) -> {
				if (value instanceof Map) {
					value = this.asMap(value);
				}
				if (key instanceof CharSequence) {
					resultMap.put(key.toString(), value);
				} else {
					resultMap.put("[" + key.toString() + "]", value);
				}
			});
		}
		return resultMap;
	}
}
