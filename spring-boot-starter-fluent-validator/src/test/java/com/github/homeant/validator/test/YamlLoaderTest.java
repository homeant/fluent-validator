package com.github.homeant.validator.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.homeant.validator.ValidatorProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
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
	public void before(){
		mapper = new ObjectMapper();
	}

	@Test
	public void test() throws IOException {
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource[] resources=resolver.getResources("classpath:validator/*.yml");
		for (int i = 0; i < resources.length; i++) {
			Resource resource = resources[i];
			List<PropertySource<?>> load = new YamlPropertySourceLoader().load(resource.getFilename(), resource);
			if(CollectionUtils.isNotEmpty(load)){
				load.stream().forEach(r->{
					log.debug("property:{}",(Map)r.getSource());
				});
			}
		}
	}

	@Test
	public void test2() throws IOException {
		List<Resource> resources = properties.getResources();
		resources.stream().forEach(resource -> {
			try {
				Map map = new Yaml().loadAs(resource.getInputStream(), Map.class);
				String value = mapper.writeValueAsString(map);
				log.debug("property:{}",value);
			} catch (IOException e) {
				e.printStackTrace();
			}

		});
	}
}
