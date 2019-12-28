package com.github.homeant.validator.core.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * EntityRule
 *
 * @author junchen
 * @date 2019-12-29 00:34
 */
@Data
public class ValidatorSpec implements Serializable {
	private String name;

	private EntityRule rule;
}
