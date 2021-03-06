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
package com.github.homeant.validator.test.service.impl;

import org.springframework.stereotype.Service;

import com.baidu.unbiz.fluentvalidator.annotation.FluentValid;
import com.github.homeant.validator.test.model.User;
import com.github.homeant.validator.test.service.IUserService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author junchen junchen1314@foxmail.com
 * @Data 2018-12-10 16:42:03
 */
@Slf4j
@Service
public class UserServiceImpl implements IUserService{
	
	@Override
	public void installUser(@FluentValid User user) {
		log.debug("user:{}",user);
	}
	
}
