package com.ecommercesystemtemplate.member.controller;

import com.ecommercesystemtemplate.common.utils.PageUtils;
import com.ecommercesystemtemplate.common.utils.R;
import com.ecommercesystemtemplate.member.entity.MemberEntity;
import com.ecommercesystemtemplate.member.service.MemberService;
import com.ecommercesystemtemplate.member.vo.MemberRegistVo;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;



/**
 * member
 *
 * @author thel.lu
 * @email lhjlslw@gmail.com
 * @date 2023-11-20 21:28:50
 */
@RestController
@RequestMapping("member/member")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    /**
     * register
     */
    @PostMapping("/register")
    public R register(@RequestBody MemberRegistVo member){
        try {
            memberService.register(member);
        } catch (Exception e) {
            return R.error();
        }

        return R.ok();
    }


    /**
     * list
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = memberService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * info
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		MemberEntity member = memberService.getById(id);

        return R.ok().put("member", member);
    }

    /**
     * save
     */
    @RequestMapping("/save")
    public R save(@RequestBody MemberEntity member){
		memberService.save(member);

        return R.ok();
    }

    /**
     * update
     */
    @RequestMapping("/update")
    public R update(@RequestBody MemberEntity member){
		memberService.updateById(member);

        return R.ok();
    }

    /**
     * delete
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		memberService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
