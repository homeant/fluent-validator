package com.github.homeant.validator.core.spring;

import com.github.homeant.validator.core.domain.Entity;
import com.github.homeant.validator.core.domain.Group;
import com.github.homeant.validator.core.domain.Property;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
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

	private static final Map<String, Group> group_cache = Maps.newConcurrentMap();

	private final List<Resource> resourcesList;

	public void init() throws IOException {
		if (CollectionUtils.isNotEmpty(resourcesList)) {
			for (Resource resource : resourcesList) {
				List<Map<String, Object>> resultList = Lists.newLinkedList();
				LoaderOptions options = new LoaderOptions();
				options.setAllowDuplicateKeys(false);
				try (InputStream inputStream = resource.getInputStream()) {
					Yaml yaml = new Yaml(new Constructor(Entity.class));
					Iterator<Object> iterator = yaml.loadAll(inputStream).iterator();
					while (iterator.hasNext()) {
						Entity rule = (Entity) iterator.next();
						Map<String, Property> property = rule.getProperty();

					}
				}
			}
		}
	}

}
