package com.github.homeant.validator.test;

import com.github.homeant.validator.core.domain.Entity;
import com.github.homeant.validator.core.domain.Property;
import com.github.homeant.validator.core.spring.ValidatorSpecContext;
import com.github.homeant.validator.test.model.User;
import com.github.homeant.validator.test.model.UserInfo;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * JsonPathTest
 *
 * @author junchen
 * @date 2019-12-29 22:34
 */
@Slf4j
@SpringBootTest
public class JsonPathTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private ValidatorSpecContext specContext;

	@Test
	public void test() {
		User user = new User();
		UserInfo userInfo = new UserInfo();
		userInfo.setAge(100);
		user.setUserInfo(userInfo);
		JsonProvider jsonProvider = Configuration.builder().jsonProvider(new JacksonJsonProvider()).build().jsonProvider();
		String json = jsonProvider.toJson(user);
		Object document = jsonProvider.parse(json);
		Integer value = JsonPath.read(document, "$.userInfo.age");
		log.debug("value:{}", value);
	}

	@Test
	public void yamlTest() {
		User user = new User();
		UserInfo userInfo = new UserInfo();
		userInfo.setAge(100);
		user.setUserInfo(userInfo);
		JsonProvider jsonProvider = Configuration.builder().jsonProvider(new JacksonJsonProvider()).build().jsonProvider();
		String json = jsonProvider.toJson(user);
		Object document = jsonProvider.parse(json);

		Yaml yaml = new Yaml(new Constructor(Entity.class));
		Entity ret = yaml.load(this.getClass().getClassLoader()
				.getResourceAsStream("validator/user.yml"));
		log.debug("ret:{}", ret);
		Map<String, Property> property = ret.getProperty();
		this.validate(property, document);
	}

	private void validate(Map<String, Property> property, Object document) {
		if (property != null) {
			property.forEach((k, v) -> {
				Object value = JsonPath.read(document, k);
				List<String> type = v.getType();
				if (CollectionUtils.isNotEmpty(type)) {
					if (type.contains("Not_Null")) {
						if (Objects.isNull(value)) {
							log.debug(k + " is null");
						}
					}
					if (type.contains("Not_Block")) {
						if (StringUtils.isBlank((String) value)) {
							log.debug(k + " is null");
						}
					}
					if (type.contains("Not_Empty")) {
						if (CollectionUtils.isEmpty((Collection<?>) value)) {
							log.debug(k + " is null");
						}
					}
				}
				String actualType = v.getActualType();
				String argumentType = v.getArgumentType();
				Map<String, Property> childProperty = v.getProperty();
				if (childProperty != null && value != null) {
					this.validate(childProperty, value);
				}
			});
		}
	}

}
