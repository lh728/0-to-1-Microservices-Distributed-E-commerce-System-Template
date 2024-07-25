package com.ecommercesystemtemplate.member.controller;

import com.ecommercesystemtemplate.common.utils.PageUtils;
import com.ecommercesystemtemplate.common.utils.R;
import com.ecommercesystemtemplate.member.entity.MemberReceiveAddressEntity;
import com.ecommercesystemtemplate.member.service.MemberReceiveAddressService;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;



/**
 * Member shipping address
 *
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-20 21:28:50
 */
@RestController
@RequestMapping("member/memberreceiveaddress")
public class MemberReceiveAddressController {
    private final MemberReceiveAddressService memberReceiveAddressService;

    public MemberReceiveAddressController(MemberReceiveAddressService memberReceiveAddressService) {
        this.memberReceiveAddressService = memberReceiveAddressService;
    }
    @GetMapping("/{id}/address")
    public List<MemberReceiveAddressEntity> getAddress(@PathVariable("id") Long id ){

        return memberReceiveAddressService.getAddress(id);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberReceiveAddressService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		MemberReceiveAddressEntity memberReceiveAddress = memberReceiveAddressService.getById(id);

        return R.ok().put("memberReceiveAddress", memberReceiveAddress);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody MemberReceiveAddressEntity memberReceiveAddress){
		memberReceiveAddressService.save(memberReceiveAddress);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody MemberReceiveAddressEntity memberReceiveAddress){
		memberReceiveAddressService.updateById(memberReceiveAddress);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		memberReceiveAddressService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
