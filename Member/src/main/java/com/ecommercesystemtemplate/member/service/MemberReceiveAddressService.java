package com.ecommercesystemtemplate.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecommercesystemtemplate.common.utils.PageUtils;
import com.ecommercesystemtemplate.member.entity.MemberReceiveAddressEntity;

import java.util.List;
import java.util.Map;

/**
 * Member shipping address
 *
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-20 21:28:50
 */
public interface MemberReceiveAddressService extends IService<MemberReceiveAddressEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<MemberReceiveAddressEntity> getAddress(Long id);
}

