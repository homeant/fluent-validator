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
package com.github.homeant.validator.boot;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.validation.Validator;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;


import com.baidu.unbiz.fluentvalidator.ValidateCallback;
import com.baidu.unbiz.fluentvalidator.interceptor.FluentValidateInterceptor;
import com.github.homeant.validator.ValidatorProperties;
import com.github.homeant.validator.callback.DefaultValidateCallback;
import com.github.homeant.validator.i18n.IMessageService;
import com.github.homeant.validator.i18n.MessageDynamicResource;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * validator auto config
 * 
 * @author junchen junchen1314@foxmail.com
 * @Data 2018-12-10 14:41:18
 */
@Data
@Slf4j
@Configuration
@ConditionalOnProperty(value = ValidatorProperties.PREFIX + ".enable", matchIfMissing = true)
@ConditionalOnClass(name = "com.github.homeant.validator.IValidator")
@AutoConfigureAfter(MessageSourceAutoConfiguration.class)
@EnableConfigurationProperties(ValidatorProperties.class)
public class ValidatorAutoConfiguration {

	private final ValidatorProperties validatorProperties;

	private static MessageSource messageSource;

	@PostConstruct
	public void init() {
		if (log.isDebugEnabled()) {
			log.debug("Init Validator");
		}
	}

	/**
	 * 国际化资源
	 * 
	 * @param messageService
	 * @return MessageSource
	 * @author junchen junchen1314@foxmail.com
	 * @Data 2018-12-10 16:04:51
	 */
	@Bean
	@ConditionalOnBean(IMessageService.class)
	public MessageSource messageSource(IMessageService messageService) {
		// MessageSourceAutoConfiguration configuration = new
		// MessageSourceAutoConfiguration();
		MessageDynamicResource resource = new MessageDynamicResource(messageService);
		resource.setParentMessageSource(messageSource);
		return resource;
	}

	/**
	 * 校验回调
	 * 
	 * @return ValidateCallback
	 * @author junchen junchen1314@foxmail.com
	 * @Data 2018-12-10 16:04:11
	 */
	@Bean
	@ConditionalOnMissingBean(ValidateCallback.class)
	public ValidateCallback callback() {
		return new DefaultValidateCallback();
	}

	/**
	 * 方法拦截器
	 * 
	 * @param validator
	 * @param callback
	 * @return FluentValidateInterceptor
	 * @author junchen junchen1314@foxmail.com
	 * @Data 2018-12-10 16:04:01
	 */
	@Bean
	@ConditionalOnBean(ValidateCallback.class)
	public FluentValidateInterceptor fluentValidateInterceptor(@Valid Validator validator, ValidateCallback callback) {
		FluentValidateInterceptor interceptor = new FluentValidateInterceptor();
		//interceptor.setLocale("zh_CN");
		interceptor.setValidator(validator);
		interceptor.setCallback(callback);
		return interceptor;
	}

	@Bean
	public BeanNameAutoProxyCreator autoProxyCreator(Environment environment) {
		boolean proxyTargetClass = environment.getProperty("spring.aop.proxy-target-class", Boolean.class, false);
		BeanNameAutoProxyCreator autoProxyCreator = new BeanNameAutoProxyCreator();
		autoProxyCreator.setProxyTargetClass(proxyTargetClass);
		autoProxyCreator.setBeanNames(validatorProperties.getBeanNames());
		autoProxyCreator.setInterceptorNames("fluentValidateInterceptor");
		return autoProxyCreator;
	}

}
