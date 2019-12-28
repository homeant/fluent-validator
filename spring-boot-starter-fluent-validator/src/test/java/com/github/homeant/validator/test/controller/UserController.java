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
package com.github.homeant.validator.test.controller;

import com.baidu.unbiz.fluentvalidator.annotation.FluentValid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.homeant.validator.test.model.User;


/**
 * @author junchen junchen1314@foxmail.com
 * @Data 2018-12-14 16:15:52
 */
@RestController
public class UserController {
	
	@PostMapping("add")
	public ResponseEntity<Object> add(@FluentValid(groups = User.Create.class) @RequestBody User user){
		return ResponseEntity.ok(user);
	}

	@PostMapping("add2")
	public ResponseEntity<Object> add2(@FluentValid(groups = User.Update.class) @RequestBody User user){
		return ResponseEntity.ok(user);
	}
}
