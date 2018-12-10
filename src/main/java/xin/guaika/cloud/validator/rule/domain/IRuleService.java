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
package xin.guaika.cloud.validator.rule.domain;

import java.util.Collections;
import java.util.List;

/**
 * @author junchen junchen1314@foxmail.com
 * @Data 2018-12-10 15:37:10
 */
public interface IRuleService {
	
	/**
	 * 通过类名得到对应实体规则
	 * @param name
	 * @return List<Rule>
	 * @author junchen junchen1314@foxmail.com
	 * @Data 2018-12-10 16:11:16
	 */
	default List<Rule> getRule(String className) {
		return Collections.emptyList();
	}

}
