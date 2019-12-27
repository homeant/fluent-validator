package com.github.homeant.validator.test.validator;

import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;
import com.baidu.unbiz.fluentvalidator.ValidatorHandler;
import org.apache.commons.validator.util.ValidatorUtils;
import org.hibernate.validator.internal.constraintvalidators.bv.NotBlankValidator;

/**
 * CommonValidator
 *
 * @author junchen
 * @date 2019-12-27 23:45
 */
public class CommonValidator extends ValidatorHandler {
	@Override
	public void compose(FluentValidator current, ValidatorContext context, Object bean) {
		String username = ValidatorUtils.getValueAsString(bean, "username");

		current.on(username,new ValidatorHandler(){
			@Override
			public boolean validate(ValidatorContext context, Object o) {
				NotBlankValidator notNullValidator = new NotBlankValidator();
				return notNullValidator.isValid(username,null);
			}
		});
	}
}
