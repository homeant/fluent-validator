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
package com.github.homeant.validator.test.model;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.baidu.unbiz.fluentvalidator.annotation.FluentValid;
import com.baidu.unbiz.fluentvalidator.annotation.FluentValidate;
import lombok.Data;

/**
 * @author junchen junchen1314@foxmail.com
 * @Data 2018-12-10 16:39:49
 */
@Data
public class User implements Serializable{
	
	private static final long serialVersionUID = -1847374915028769973L;

	@NotNull
	private String username;
	
	private String password;

	/**
	 * 如果级联校验中有 {@link FluentValidate}，那么使用 {@link FluentValid}
	 * 如果级联校验中没有 {@link FluentValidate},那么使用 {@link Valid}
	 */
	@Valid
	private UserInfo userInfo;
}
