package com.github.homeant.validator.test.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * UserInfo
 *
 * @author junchen
 * @date 2019-12-28 15:35
 */
@Data
public class UserInfo implements Serializable {
	@NotNull(groups = User.Create.class)
	private Integer age;
}
