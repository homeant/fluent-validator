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

import org.springframework.beans.factory.annotation.Autowired;
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
import com.baidu.unbiz.fluentvalidator.interceptor.FluentValidateInterceptor;
import com.baidu.unbiz.fluentvalidator.support.MessageSupport;
import com.github.homeant.validator.IValidator;
import com.github.homeant.validator.spring.ValidatorBeanPostProcessor;
import com.github.homeant.validator.ValidatorProperties;
import com.github.homeant.validator.callback.DefaultValidateCallback;
import com.github.homeant.validator.i18n.IMessageService;
import com.github.homeant.validator.i18n.MessageDynamicResource;

import lombok.Data;

/**
 * validator auto config
 * 
 * @author junchen junchen1314@foxmail.com
 * @Data 2018-12-10 14:41:18
 */
@Data
@Configuration
@ConditionalOnClass(value= {IValidator.class,com.baidu.unbiz.fluentvalidator.Validator.class})
@ConditionalOnProperty(value = ValidatorProperties.PREFIX + ".enable", matchIfMissing = true)
@AutoConfigureBefore({MessageSourceAutoConfiguration.class})
@EnableConfigurationProperties(ValidatorProperties.class)
public class ValidatorAutoConfiguration {

	private final ValidatorProperties validatorProperties;
	
	@Autowired
	private MessageSource messageSource;

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
		MessageDynamicResource resource = new MessageDynamicResource(messageService);
		resource.setParentMessageSource(messageSource);
		return resource;
	}
	
	@Bean
	@ConditionalOnMissingBean(IMessageService.class)
	public MessageSupport messageSupport() {
		MessageSupport support = new MessageSupport();
		support.setMessageSource(messageSource);
		return support;
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
        validateInterceptor.setCallback(callback);
        validateInterceptor.setHibernateDefaultErrorCode(10000);
        return validateInterceptor;
	}
	
    @Bean
    @ConditionalOnMissingBean
    public ValidatorBeanPostProcessor validatorBeanPostProcessor(FluentValidateInterceptor fluentValidateInterceptor,Environment environment) {
    	ValidatorBeanPostProcessor postProcessor = new ValidatorBeanPostProcessor();
    	boolean proxyTargetClass = environment.getProperty("spring.aop.proxy-target-class", Boolean.class, true);
    	postProcessor.setProxyTargetClass(proxyTargetClass);
    	postProcessor.setFluentValidateInterceptor(fluentValidateInterceptor);
    	return postProcessor;
    }

}
