/**
 * Copyright (c) 2011-2014, junchen (junchen1314@foxmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.github.homeant.validator.test;


import com.github.homeant.validator.core.exception.ValidateFailException;
import com.github.homeant.validator.test.model.User;
import com.github.homeant.validator.test.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.executable.ExecutableValidator;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * @author junchen junchen1314@foxmail.com
 * @Data 2018-12-06 14:33:54
 */
@Slf4j
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ValidatorTest extends AbstractTestNGSpringContextTests {


	@Autowired
	private IUserService userService;
	
	@Autowired
	private Validator validator;

	@BeforeMethod
	public void init() throws Exception {
		log.debug("validator:{}",validator);
		log.debug("init ...");
	}

	@Test
	public void test() {
		try {
			User user = new User();
			userService.installUser(user);
		} catch (Exception e) {
			if (e instanceof ValidateFailException) {
				ValidateFailException exception = (ValidateFailException) e;
				exception.getErrors().forEach(r -> {
					log.debug("errors:{}{}", r.getField(), r.getErrorMsg());
				});
			} else {
				log.debug("ex", e);
			}
		}
	}

	public void validator() throws NoSuchMethodException {
		ExecutableValidator executableValidator = validator.forExecutables();
		Method method = User.class
				.getMethod("setUsername", String.class);
		Set<ConstraintViolation<User>> violations
				= executableValidator.validateParameters(new User(), method, new Object[]{});
	}

	@Configuration
	@ComponentScan(basePackages = "com.github.homeant")
	@EnableAutoConfiguration
	public static class Config {
		 
	}
}
