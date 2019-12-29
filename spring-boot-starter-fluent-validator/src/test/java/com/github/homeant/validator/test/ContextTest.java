package com.github.homeant.validator.test;

import com.github.homeant.validator.core.domain.EntityRule;
import com.github.homeant.validator.core.spring.ValidatorSpecContext;
import com.github.homeant.validator.test.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

/**
 * ContextTest
 *
 * @author junchen
 * @date 2019-12-29 14:36
 */
@Slf4j
@SpringBootTest
public class ContextTest extends AbstractTestNGSpringContextTests {

	@Autowired
	private ValidatorSpecContext context;

	@Test
	public void test(){
		EntityRule rule = context.get(User.class);
		log.debug("rule:{}",rule);
	}
}
