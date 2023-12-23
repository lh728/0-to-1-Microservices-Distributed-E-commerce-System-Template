/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.ecommercesystemtemplate.common.group;


import jakarta.validation.GroupSequence;

/**
 * Define the validation order; if the AddGroup fails, the UpdateGroup will not be validated further.
 *
 * @author Mark sunlightcs@gmail.com
 */
@GroupSequence({AddGroup.class, UpdateGroup.class})
public interface Group {

}
