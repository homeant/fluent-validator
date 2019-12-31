/**
 * Copyright (c) 2011-2014, junchen (junchen1314@foxmail.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.github.homeant.validator.boot;


import com.github.homeant.validator.core.processor.FluentValidatorPostProcessor;
import com.github.homeant.validator.core.spring.FluentValidateInterceptor;
import com.github.homeant.validator.core.spring.ValidatorSpecContext;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
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
import com.baidu.unbiz.fluentvalidator.support.MessageSupport;
import com.github.homeant.validator.core.spring.ValidatorBeanPostProcessor;
import com.github.homeant.validator.ValidatorProperties;
import com.github.homeant.validator.core.callback.DefaultValidateCallback;
import com.github.homeant.validator.core.i18n.IMessageService;
import com.github.homeant.validator.core.i18n.MessageDynamicResource;

import lombok.Data;

import javax.validation.Validator;
import java.io.IOException;

/**
 * validator auto config
 *
 * @author junchen junchen1314@foxmail.com
 * @Data 2018-12-10 14:41:18
 */
@Data
@Configuration
@ConditionalOnClass(value = {com.baidu.unbiz.fluentvalidator.Validator.class})
@ConditionalOnProperty(prefix = ValidatorProperties.PREFIX, value = "enable", matchIfMissing = true, havingValue = "true")
@AutoConfigureBefore({MessageSourceAutoConfiguration.class})
@EnableConfigurationProperties({ValidatorProperties.class})
public class ValidatorAutoConfiguration {

	private final ValidatorProperties validatorProperties;

	private final Validator validator;

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
	public MessageSource messageSource(IMessageService messageService, MessageSource messageSource) {
		MessageDynamicResource resource = new MessageDynamicResource(messageService);
		resource.setParentMessageSource(messageSource);
		return resource;
	}

	@Bean
	@ConditionalOnMissingBean(IMessageService.class)
	public MessageSupport messageSupport(MessageSource messageSource) {
		MessageSupport support = new MessageSupport();
		support.setMessageSource(messageSource);
		return support;
	}

	@Bean(initMethod = "init")
	public ValidatorSpecContext specContext(ValidatorProperties properties) throws IOException {
		return new ValidatorSpecContext(properties.getResources());
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

	@Bean
	@ConditionalOnBean({ValidateCallback.class})
	public FluentValidateInterceptor fluentValidateInterceptor(ValidateCallback callback) {
		FluentValidateInterceptor validateInterceptor = new FluentValidateInterceptor();
		validateInterceptor.setFluentValidatorPostProcessor(new FluentValidatorPostProcessor());
		validateInterceptor.setCallback(callback);
		validateInterceptor.setValidator(validator);
		validateInterceptor.setHibernateDefaultErrorCode(validatorProperties.getHibernateDefaultErrorCode());
		return validateInterceptor;
	}

	@Bean
	@ConditionalOnMissingBean
	public ValidatorBeanPostProcessor validatorBeanPostProcessor(FluentValidateInterceptor fluentValidateInterceptor, Environment environment) {
		ValidatorBeanPostProcessor postProcessor = new ValidatorBeanPostProcessor();
		boolean proxyTargetClass = environment.getProperty("spring.aop.proxy-target-class", Boolean.class, true);
		postProcessor.setProxyTargetClass(proxyTargetClass);
		postProcessor.setFluentValidateInterceptor(fluentValidateInterceptor);
		return postProcessor;
	}

}
