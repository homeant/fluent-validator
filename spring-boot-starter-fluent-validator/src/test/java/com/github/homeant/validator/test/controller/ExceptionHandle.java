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


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.github.homeant.validator.core.exception.ValidateFailException;

import lombok.extern.slf4j.Slf4j;

/**
 * @author junchen junchen1314@foxmail.com
 * @Data 2018-11-23 15:51:28
 */
@Slf4j
@RestControllerAdvice
public class ExceptionHandle {
	
	@ExceptionHandler(value = ValidateFailException.class)
    public ResponseEntity<Object> Handle(ValidateFailException e){
		return ResponseEntity.status(400).body(e.getErrors());
    }
}
