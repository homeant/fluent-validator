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

import org.springframework.boot.context.properties.ConfigurationProperties;


import lombok.Data;

/**
 * @author junchen junchen1314@foxmail.com
 * @Data 2018-12-10 14:43:12
 */
@Data
@ConfigurationProperties(prefix = ValidatorProperties.PREFIX)
public class ValidatorProperties {
	
	public static final String PREFIX = "validator";
	
	private Boolean enable = true;
	
	private Boolean failFast = true;
	
	private String [] beanNames = new String[0];
}
