package com.github.homeant.validator.core.processor;

import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.support.MethodNameFluentValidatorPostProcessor;
import lombok.Data;
import org.aopalliance.intercept.MethodInvocation;

/**
 * FluentValidatorPostProcessor
 *
 * @author junchen
 * @date 2019-12-27 23:39
 */
@Data
public class FluentValidatorPostProcessor extends MethodNameFluentValidatorPostProcessor {

	@Override
	public FluentValidator postProcessBeforeDoValidate(FluentValidator f, MethodInvocation methodInvocation) {
		FluentValidator fluentValidator = super.postProcessBeforeDoValidate(f, methodInvocation);
		return fluentValidator;
	}
}
