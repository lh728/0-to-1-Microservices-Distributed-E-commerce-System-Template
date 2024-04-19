package com.ecommercesystemtemplate.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecommercesystemtemplate.common.utils.PageUtils;
import com.ecommercesystemtemplate.common.utils.Query;
import com.ecommercesystemtemplate.member.dao.MemberDao;
import com.ecommercesystemtemplate.member.dao.MemberLevelDao;
import com.ecommercesystemtemplate.member.entity.MemberEntity;
import com.ecommercesystemtemplate.member.entity.MemberLevelEntity;
import com.ecommercesystemtemplate.member.exception.PhoneExistException;
import com.ecommercesystemtemplate.member.exception.UserNameExistException;
import com.ecommercesystemtemplate.member.service.MemberService;
import com.ecommercesystemtemplate.member.vo.MemberRegistVo;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("memberService")
public class MemberServiceImpl extends ServiceImpl<MemberDao, MemberEntity> implements MemberService {

    final MemberLevelDao memberLevelDao;

    public MemberServiceImpl(MemberLevelDao memberLevelDao) {
        this.memberLevelDao = memberLevelDao;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<MemberEntity> page = this.page(
                new Query<MemberEntity>().getPage(params),
                new QueryWrapper<MemberEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void register(MemberRegistVo member) {
        MemberEntity memberEntity = new MemberEntity();

        // set default level
        MemberLevelEntity entity =  memberLevelDao.getDefaultLevel();
        memberEntity.setLevelId(entity.getId());

        // set phone number attr
        memberEntity.setMobile(member.getPhone());
        // set username attr
        memberEntity.setUsername(member.getUserName());

        // check username and phonenumber uniqueness
        checkPhoneUnique(member.getPhone());
        checkUsernameUnique(member.getUserName());

        // set password (Encrypted storage)



        this.baseMapper.insert(memberEntity);
    }

    @Override
    public void checkUsernameUnique(String username) throws UserNameExistException{
        Integer integer = this.baseMapper.selectCount(new QueryWrapper<MemberEntity>().eq("username", username));
        if (integer > 0){
            throw new UserNameExistException();
        }
    }

    @Override
    public void checkPhoneUnique(String phone) throws PhoneExistException {
        Integer mobile = this.baseMapper.selectCount(new QueryWrapper<MemberEntity>().eq("mobile", phone));
        if (mobile > 0){
            throw new PhoneExistException();
        }
    }

}
