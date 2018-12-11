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
package com.github.homeant.validator;

import com.baidu.unbiz.fluentvalidator.Composable;
import com.baidu.unbiz.fluentvalidator.FluentValidator;
import com.baidu.unbiz.fluentvalidator.Validator;
import com.baidu.unbiz.fluentvalidator.ValidatorContext;

/**
 * @author junchen junchen1314@foxmail.com
 * @Data 2018-12-06 14:36:38
 */
public interface IValidator<T> extends Validator<T>, Composable<T>{
	@Override
	default boolean accept(ValidatorContext context, T t) {
		return true;
	}

	@Override
	default boolean validate(ValidatorContext context, T t) {
		return true;
	}

	@Override
	default void onException(Exception e, ValidatorContext context, T t) {
		 
	}
	
	@Override
	default void compose(FluentValidator current, ValidatorContext context, T t) {
        // extension point for clients to add more validators to the current fluent chain
    }
}
