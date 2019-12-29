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

import com.google.common.collect.Lists;
import org.springframework.boot.context.properties.ConfigurationProperties;


import lombok.Data;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.List;

/**
 * @author junchen junchen1314@foxmail.com
 * @Data 2018-12-10 14:43:12
 */
@Data
@ConfigurationProperties(prefix = ValidatorProperties.PREFIX)
public class ValidatorProperties {
	
	public static final String PREFIX = "fluent-validator";
	
	private Boolean enable = true;

	private Integer hibernateDefaultErrorCode = 10000;

	private String[] rulePaths = {"classpath*:validator/**/*.yml","classpath*:validator/**/*.yaml"};

	public List<Resource> getResources() throws IOException {
		List<Resource> resourceList = Lists.newArrayList();
		if (rulePaths != null && rulePaths.length > 0) {
			ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
			for (int i = 0; i < rulePaths.length; i++) {
				Resource[] resources = resourcePatternResolver.getResources(rulePaths[i]);
				resourceList.addAll(Lists.newArrayList(resources));
			}
		}
		return resourceList;
	}

}
