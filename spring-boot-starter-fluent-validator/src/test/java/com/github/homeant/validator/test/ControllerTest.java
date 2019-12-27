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


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.validation.Validator;

/**
 * @author junchen junchen1314@foxmail.com
 * @Data 2018-12-14 16:24:12
 */
@Slf4j
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ControllerTest extends AbstractTestNGSpringContextTests {
	
	@Autowired
    private WebApplicationContext context;

	@Autowired
	private Validator validator;
	
	MockMvc mockMvc;
	
	@BeforeMethod
	public void before() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}
	
	@Test
	public void test() throws Exception {
	      MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/add").accept(MediaType.APPLICATION_JSON)).andReturn();
	      log.debug("result:{}",mvcResult.getResponse().getContentAsString());
	}
	
	@Configuration
	@ComponentScan(basePackages = "com.github.homeant")
	@EnableAutoConfiguration
	public static class Config {
		 
		
	}
	
}
