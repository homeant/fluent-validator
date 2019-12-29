package com.github.homeant.validator.test;

import com.github.homeant.validator.core.domain.EntityRule;
import com.github.homeant.validator.core.spring.ValidatorSpecContext;
import com.github.homeant.validator.test.model.User;
import com.github.homeant.validator.test.model.UserInfo;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.ParseContext;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import java.util.Map;

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
		log.debug("value:{}",value);
	}

	@Test
	public void validatorTest(){
		EntityRule rule = specContext.get(User.class);
		Configuration configuration = Configuration.builder().jsonProvider(new JacksonJsonProvider()).build();
		DocumentContext context = JsonPath.using(configuration).parse(rule.getProperty());
		log.debug("result:{}",context.read("$.userInfo.property", Map.class));
	}


}
