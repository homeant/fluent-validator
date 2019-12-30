package com.github.homeant.validator.core.spring;

import com.github.homeant.validator.core.domain.EntityRule;
import com.github.homeant.validator.core.domain.PropertySpec;
import com.github.homeant.validator.core.util.MapUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

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
					Yaml yaml = new Yaml(new Constructor(EntityRule.class));
					Iterator<Object> iterator = yaml.loadAll(inputStream).iterator();
					while (iterator.hasNext()) {
						EntityRule rule = (EntityRule) iterator.next();
						rule_cache.put(rule.getActualType(), rule);
						Map<String, PropertySpec> property = rule.getProperty();
						if(property!=null){
							this.build(property, null);
							property_cache.put(rule.getActualType(), MapUtils.flattenedMap((Map) property, null));
						}
					}
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
			Map<String, PropertySpec> property = rule.getProperty();
			if (property != null) {
				this.build(property, null);
				property_cache.put(rule.getActualType(), MapUtils.flattenedMap((Map) property, null));
			}
		}
	}

	public Boolean has(@NonNull Class clazz) {
		return rule_cache.containsKey(clazz.getCanonicalName());
	}

	private void build(Map<String, PropertySpec> propertySpecMap, PropertySpec parentSpec) {
		if (propertySpecMap != null) {
			propertySpecMap.forEach((String k, PropertySpec v) -> {
				v.setName(k);
				if (parentSpec != null) {
					v.setParentSpec(parentSpec);
				}
				Map<String, PropertySpec> property = v.getProperty();
				if (property != null) {
					this.build(property, v);
				}
			});
		}
	}
}
