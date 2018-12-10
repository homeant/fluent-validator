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
package xin.guaika.cloud.validator.impl;


import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.baidu.unbiz.fluentvalidator.ValidationError;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.baidu.unbiz.fluentvalidator.support.MessageSupport;
import com.google.common.base.Joiner;

import groovy.lang.Closure;
import groovy.lang.GroovyShell;
import groovy.util.Expando;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import xin.guaika.cloud.validator.IValidator;
import xin.guaika.cloud.validator.ValidatorProperties;
import xin.guaika.cloud.validator.rule.domain.IRuleService;
import xin.guaika.cloud.validator.rule.domain.Rule;

/**
 * @author junchen junchen1314@foxmail.com
 * @Data 2018-12-06 14:35:24
 */
@Data
@Slf4j
@EnableConfigurationProperties(ValidatorProperties.class)
public class DefaultValidator implements IValidator<Object> {

	private final IRuleService ruleService;
	
	private Map<String,List<Rule>> rules = new ConcurrentHashMap<>();
	
	private ValidatorProperties properties;
	
	@Override
	public boolean validate(ValidatorContext context, Object t) {
		if(log.isDebugEnabled()) {
			log.debug("validator {}",t.getClass().getName());
		}
		List<Rule> list = ruleService.getRule(t.getClass().getName());
		boolean result = true;
		if(list!=null && list.size()>0) {
			for (Rule rule : list) {
				String code = rule.getMessageCode();
				String text = rule.getMessageText();
				String field = rule.getField();
				if("string".equals(rule.getType())) {
					Boolean flag = isNotBlank(t,rule.getField());
					if(!flag) {
						result = false;
						if(StringUtils.isNotBlank(code)) {
							text = MessageSupport.getText(code,field);
						}
						context.addError(ValidationError.create(text).setField(field).setInvalidValue(t));
					}
				}
				if(!result&&properties.getFailFast()) {
					break;
				}
			}
		}
		return result;
	}
	
	/**
	 * 正则表达式
	 * @param source
	 * @param field
	 * @param expression
	 * @return Boolean
	 * @author junchen junchen1314@foxmail.com
	 * @Data 2018-12-07 17:12:50
	 */
	private Boolean checkRegExp(Object source,String field,String expression) {
		String script = "bean."+field+" ==~ "+expression;
		return (Boolean) evaluateGroovy(script,source);
	}
	
	/**
	 * 判断空字符串
	 * @param source
	 * @param field
	 * @return Boolean
	 * @author junchen junchen1314@foxmail.com
	 * @Data 2018-12-07 17:26:24
	 */
	private Boolean isNotBlank(Object source,String field) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("return org.apache.commons.lang3.StringUtils.isNotBlank(bean."+field+")");
		return (Boolean) evaluateGroovy(buffer.toString(),source);
	}
	
	
	/**
	 * 判断空
	 * @param source
	 * @param field
	 * @return Boolean
	 * @author junchen junchen1314@foxmail.com
	 * @Data 2018-12-07 17:13:00
	 */
	private Boolean isNotEmpty(Object source,String field) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("return org.apache.commons.lang3.StringUtils.isNotEmpty(bean."+field+")");
		return (Boolean) evaluateGroovy(buffer.toString(),source);
	}
	
	
	@SuppressWarnings("unchecked")
	private <T> Object evaluateGroovy(String script,Object source) {
		GroovyShell shell = new GroovyShell();
		Closure<T> closure = (Closure<T>)shell.evaluate(Joiner.on("").join("{it ->",script,"}"));
		Closure<T> shellResult = closure.rehydrate(new Expando(), null, null);
		shellResult.setResolveStrategy(Closure.DELEGATE_ONLY);
		shellResult.setProperty("bean",source);
        return shellResult.call();
	}
	
	
}
