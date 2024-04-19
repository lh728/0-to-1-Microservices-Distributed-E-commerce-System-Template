package com.ecommercesystemtemplate.member.dao;

import com.ecommercesystemtemplate.member.entity.MemberLevelEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * member level
 *
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-20 21:28:50
 */
@Mapper
public interface MemberLevelDao extends BaseMapper<MemberLevelEntity> {

    MemberLevelEntity getDefaultLevel();
}
