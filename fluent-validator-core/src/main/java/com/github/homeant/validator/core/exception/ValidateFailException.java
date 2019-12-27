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
package com.github.homeant.validator.core.exception;

import java.util.List;

import com.baidu.unbiz.fluentvalidator.ValidationError;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 校验异常类
 * @author junchen junchen1314@foxmail.com
 * @Data 2018-12-06 16:40:30
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class ValidateFailException extends RuntimeException{

	private static final long serialVersionUID = 6113950199369314904L;
	
	private List<ValidationError> errors;
	
	public ValidateFailException(String message) {
		super(message);
	}
	
	public ValidateFailException(Throwable e) {
		super(e);
	}
	
	public ValidateFailException(String message,Throwable e) {
		super(message, e);
	}
}
