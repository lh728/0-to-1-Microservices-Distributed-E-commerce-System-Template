package com.ecommercesystemtemplate.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ecommercesystemtemplate.common.utils.PageUtils;
import com.ecommercesystemtemplate.member.entity.MemberEntity;
import com.ecommercesystemtemplate.member.exception.PhoneExistException;
import com.ecommercesystemtemplate.member.exception.UserNameExistException;
import com.ecommercesystemtemplate.member.vo.MemberLoginVo;
import com.ecommercesystemtemplate.member.vo.MemberRegistVo;
import com.ecommercesystemtemplate.member.vo.SocialUser;

import java.util.Map;

/**
 * member
 *
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-20 21:28:50
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void register(MemberRegistVo member);

    void checkUsernameUnique(String username) throws UserNameExistException;

    void checkPhoneUnique(String phone) throws PhoneExistException;

    MemberEntity login(MemberLoginVo vo);

    MemberEntity login(SocialUser vo) throws Exception;
}

