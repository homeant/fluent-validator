package com.github.homeant.validator.core.domain;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * PropertySpec
 *
 * @author junchen
 * @date 2019-12-29 10:37
 */
@Data
@ToString
public class Property implements Serializable {
	/**
	 * 类型
	 */
	private String actualType;
	/**
	 * 泛型的参数类型
	 */
	private String argumentType;

	private Map<String, Property> property;

	private List<String> group;

	/**
	 * 可取类型
	 * not-null，email，not-blank，not-empty
	 */
	private List<String> type;
	/**
	 * 正则
	 */
	private String regexp;
	/**
	 * list范围
	 */
	private Extent size;
	/**
	 * 字符范围
	 */
	private Extent length;
	/**
	 * int类型范围
	 */
	private Extent range;

	private String validator;

	private String expression;
}
