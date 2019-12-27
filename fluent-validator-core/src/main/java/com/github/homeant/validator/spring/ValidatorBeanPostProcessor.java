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
package com.github.homeant.validator.spring;

import java.lang.annotation.Annotation;


import org.springframework.aop.framework.AbstractAdvisingBeanPostProcessor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import com.baidu.unbiz.fluentvalidator.annotation.FluentValid;
import com.baidu.unbiz.fluentvalidator.interceptor.FluentValidateInterceptor;
import com.github.homeant.validator.spring.ValidatorPointcutAdvisor;

import lombok.Getter;
import lombok.Setter;

/**
 * @author junchen junchen1314@foxmail.com
 * @Data 2018-12-13 14:11:37
 */
public class ValidatorBeanPostProcessor extends AbstractAdvisingBeanPostProcessor implements BeanFactoryAware {
	
	private static final long serialVersionUID = -3858983354365245870L;

	@Getter
	@Setter
	private Class<? extends Annotation> validatedAnnotationType = FluentValid.class;
	
	@Getter
	@Setter
	private FluentValidateInterceptor fluentValidateInterceptor;

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		setBeforeExistingAdvisors(true);
		ValidatorPointcutAdvisor pointcutAdvisor = new ValidatorPointcutAdvisor(validatedAnnotationType, fluentValidateInterceptor);
		pointcutAdvisor.setBeanFactory(beanFactory);
        this.advisor = pointcutAdvisor;
	}
	
}
