package com.github.homeant.validator.core;

import com.baidu.unbiz.fluentvalidator.ValidationError;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.baidu.unbiz.fluentvalidator.ValidatorHandler;
import com.github.homeant.validator.core.domain.EntityRule;
import com.github.homeant.validator.core.domain.PropertySpec;
import com.github.homeant.validator.core.spring.ValidatorSpecContext;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import lombok.Data;
import org.springframework.cglib.beans.BeanMap;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;


/**
 * CoreValidator
 *
 * @author junchen
 * @date 2019-12-29 14:51
 */
@Data
public class CoreValidator extends ValidatorHandler {

	private final ValidatorSpecContext specContext;

	JsonProvider jsonProvider = Configuration.builder().jsonProvider(new JacksonJsonProvider()).build().jsonProvider();

	@Override
	public boolean accept(ValidatorContext context, Object bean) {
		if (specContext == null || bean == null) {
			return false;
		}
		if (bean instanceof Collection) {
			if (!((Collection) bean).isEmpty()) {
				Iterator iterator = ((Collection) bean).iterator();
				if (iterator.hasNext()) {
					Object result = iterator.next();
					return specContext.has(result.getClass());
				}
			}
			return false;
		} else {
			return specContext.has(bean.getClass());
		}
	}

	@Override
	public boolean validate(ValidatorContext context, Object bean) {
		if(bean instanceof Collection){
			Collection collection = (Collection)bean;
		}else{
			Class clazz = bean.getClass();

		}

		return true;
	}

	private boolean validate(ValidatorContext context, Map<String,String> map) {
		return false;
	}
	
}
