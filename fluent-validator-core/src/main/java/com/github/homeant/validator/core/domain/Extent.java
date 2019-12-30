package com.github.homeant.validator.core.domain;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * Extent
 *
 * @author junchen
 * @date 2019-12-30 23:37
 */
@Data
@ToString
public class Extent implements Serializable {
	private Integer min;
	private Integer max;
}
