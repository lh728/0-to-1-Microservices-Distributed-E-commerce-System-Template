package com.ecommercesystemtemplate.member.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecommercesystemtemplate.common.utils.HttpUtils;
import com.ecommercesystemtemplate.common.utils.PageUtils;
import com.ecommercesystemtemplate.common.utils.Query;
import com.ecommercesystemtemplate.member.dao.MemberDao;
import com.ecommercesystemtemplate.member.dao.MemberLevelDao;
import com.ecommercesystemtemplate.member.entity.MemberEntity;
import com.ecommercesystemtemplate.member.entity.MemberLevelEntity;
import com.ecommercesystemtemplate.member.exception.PhoneExistException;
import com.ecommercesystemtemplate.member.exception.UserNameExistException;
import com.ecommercesystemtemplate.member.service.MemberService;
import com.ecommercesystemtemplate.member.vo.MemberLoginVo;
import com.ecommercesystemtemplate.member.vo.MemberRegistVo;
import com.ecommercesystemtemplate.member.vo.SocialUser;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
        memberEntity.setNickname(member.getUserName());

        // check username and phonenumber uniqueness
        checkPhoneUnique(member.getPhone());
        checkUsernameUnique(member.getUserName());

        // set password (Encrypted storage MD5 hashing with salt)
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode(member.getPassword());
        memberEntity.setPassword(encode);

        // set other default values

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

    @Override
    public MemberEntity login(MemberLoginVo vo) {
        String loginAccount = vo.getLoginAccount();
        String password = vo.getPassword();

        // query the database to retrieve the user password
        MemberEntity memberEntity = this.baseMapper.selectOne(new QueryWrapper<MemberEntity>().eq("username", loginAccount).or().eq("mobile", loginAccount));
        if (memberEntity != null){
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            boolean matches = bCryptPasswordEncoder.matches(password, memberEntity.getPassword());
            if (matches){
                return memberEntity;
            }else {
                // if the password does not match, return null
                return null;
            }
        }else {
            // if the user does not exist, return null
            return null;
        }
    }

    @Override
    public MemberEntity login(SocialUser vo) throws Exception {
        // combine login and registration logic
        String uid = vo.getUid();
        // 1. check if the user has logged in before
        MemberEntity memberEntity = this.baseMapper.selectOne(new QueryWrapper<MemberEntity>().eq("social_uid", uid));
        if (memberEntity != null){
            MemberEntity entity = new MemberEntity();
            entity.setId(memberEntity.getId());
            entity.setAccessToken(vo.getAccess_token());
            entity.setExpiresIn(vo.getExpires_in());

            this.baseMapper.updateById(entity);
            memberEntity.setAccessToken(vo.getAccess_token());
            memberEntity.setExpiresIn(vo.getExpires_in());
            return memberEntity;
        } else {
            // 2. register
            MemberEntity entity = new MemberEntity();
            try{
                // 3. query current social account information
                Map<String,String> queryMap = new HashMap<>();
                queryMap.put("access_token",vo.getAccess_token());
                queryMap.put("social_uid",vo.getUid());
                HttpResponse response = HttpUtils.doGet("https://api.weibo.com", "/2/users/show.json", "get", new HashMap<String, String>(), queryMap);
                if (response.getStatusLine().getStatusCode() == 200){
                    String json = EntityUtils.toString(response.getEntity());
                    JSONObject jsonObject = JSON.parseObject(json);
                    String name = jsonObject.getString("name");
                    String gender = jsonObject.getString("gender");
                    entity.setNickname(name);
                    entity.setGender(gender.equals("m") ? 1 : 0);
                }
            }catch (Exception e){
            }
            entity.setSocialUid(vo.getUid());
            entity.setAccessToken(vo.getAccess_token());
            entity.setExpiresIn(vo.getExpires_in());
            this.baseMapper.insert(entity);
            return entity;
        }
    }

}
