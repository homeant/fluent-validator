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
package xin.guaika.cloud.validator.i18n;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;

import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;


import lombok.Data;
import lombok.EqualsAndHashCode;
import xin.guaika.cloud.validator.i18n.domain.MessageResource;

/**
 * 动态国际化
 * @author junchen junchen1314@foxmail.com
 * @Data 2018-12-07 10:55:14
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class MessageDynamicResource extends AbstractMessageSource implements ResourceLoaderAware{
	
	private ResourceLoader resourceLoader;
	
	private final IMessageService messageService;
	
	private static final Map<String, Map<Locale, MessageFormat>> LOCAL_CACHE = new ConcurrentHashMap<>(256);
	
	@PostConstruct
    public void init() {
        this.reload();
    }
	
	public void reload() {
		LOCAL_CACHE.clear();
		List<MessageResource> list = messageService.getAllMessage();
		Optional.of(list).get().forEach(r->{
			Locale locale = parseLocaleValue(r.getLanguage());
			Map<Locale, MessageFormat> map = LOCAL_CACHE.getOrDefault(r.getCode(),new ConcurrentHashMap<>());
			map.put(locale,createMessageFormat(r.getMessage(), locale));
			LOCAL_CACHE.put(r.getCode(), map);
		});
		
	}
	
	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	@Override
	protected MessageFormat resolveCode(String code, Locale locale) {
		Map<Locale, MessageFormat> map = LOCAL_CACHE.get(code);
		if(map!=null) {
			MessageFormat format = map.get(locale);
			return format;
		}
		return null;
	}
	
	private Locale parseLocaleValue(String locale) {
		return StringUtils.parseLocaleString(locale);
	}
	
}
