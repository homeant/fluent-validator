package com.github.homeant.validator.core.domain;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Map;

/**
 * ElementRule
 *
 * @author junchen
 * @date 2019-12-29 00:29
 */
@Data
@ToString
public class EntityRule implements Serializable {
	private String actualType;

	private Map<String,GroupSpec> group;

	private Map<String,PropertySpec> property;

}
