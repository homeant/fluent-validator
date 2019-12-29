package com.github.homeant.validator.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.homeant.validator.ValidatorProperties;
import com.github.homeant.validator.core.domain.EntityRule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.collections.Lists;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * YamlLoaderTest
 *
 * @author junchen
 * @date 2019-12-28 22:34
 */
@Slf4j
@SpringBootTest
public class YamlLoaderTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private ValidatorProperties properties;

	private ObjectMapper mapper;

	@BeforeMethod
	public void before() {
		mapper = new ObjectMapper();
	}

	@Test
	public void test() throws IOException {
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources = resolver.getResources("classpath:validator/*.yml");
		for (int i = 0; i < resources.length; i++) {
			Resource resource = resources[i];
			List<PropertySource<?>> load = new YamlPropertySourceLoader().load(resource.getFilename(), resource);

			if (CollectionUtils.isNotEmpty(load)) {
				load.stream().forEach(r -> {
					log.debug("property:{}", (Map) r.getSource());
				});
			}
		}
	}

	@Test
	public void mapTest() throws IOException {
		List<Resource> resources = properties.getResources();
		List<Map<String, Object>> resultList = Lists.newLinkedList();
		resources.stream().forEach(resource -> {
			try {
				LoaderOptions options = new LoaderOptions();
				options.setAllowDuplicateKeys(false);
				Iterator<Object> iterator = new Yaml(options).loadAll(resource.getInputStream()).iterator();

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
				log.debug("result:{}", resultList);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		EntityRule entityRule = new EntityRule();
		BeanMap beanMap = BeanMap.create(entityRule);
		beanMap.putAll(resultList.get(0));
		log.debug("rule:{}",entityRule);
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
