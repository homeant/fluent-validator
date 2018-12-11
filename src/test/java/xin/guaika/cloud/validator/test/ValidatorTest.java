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
package xin.guaika.cloud.validator.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;
import xin.guaika.cloud.validator.exception.ValidateFailException;
import xin.guaika.cloud.validator.test.model.User;
import xin.guaika.cloud.validator.test.service.IUserService;

/**
 * @author junchen junchen1314@foxmail.com
 * @Data 2018-12-06 14:33:54
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ValidatorTest {


	@Autowired
	private IUserService userService;

	@Before
	public void init() throws Exception {
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
				log.debug("", e);
			}
		}
	}

	@Configuration
	@ComponentScan(basePackages = "xin.guaika.cloud")
	@EnableAutoConfiguration
	public static class Config {

	}
}
