package com.github.homeant.validator.core.spring;

import com.github.homeant.validator.core.domain.ValidatorSpec;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.NonNull;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * ValidatorBeanFactory
 *
 * @author junchen
 * @date 2019-12-28 23:52
 */
@Data
public class ValidatorSpecContext {

	private static final Map<String, ValidatorSpec> CACHE = Maps.newConcurrentMap();

	private final List<Resource> resourcesList;

	public void init() throws IOException {
		if (CollectionUtils.isNotEmpty(resourcesList)) {
			for (Resource resource : resourcesList) {
				ValidatorSpec rule = new Yaml().loadAs(resource.getInputStream(), ValidatorSpec.class);
				CACHE.put(rule.getName(), rule);
			}
		}
	}

	public ValidatorSpec get(String name) {
		return CACHE.get(name);
	}

	public void put(@NonNull ValidatorSpec spec) {
		if (StringUtils.isNotBlank(spec.getName())) {
			CACHE.put(spec.getName(), spec);
		}
	}
}
