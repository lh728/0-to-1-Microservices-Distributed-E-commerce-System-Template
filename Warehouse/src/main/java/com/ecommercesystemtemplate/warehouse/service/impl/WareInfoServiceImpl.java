package com.ecommercesystemtemplate.warehouse.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecommercesystemtemplate.common.utils.PageUtils;
import com.ecommercesystemtemplate.common.utils.Query;
import com.ecommercesystemtemplate.common.utils.R;
import com.ecommercesystemtemplate.warehouse.dao.WareInfoDao;
import com.ecommercesystemtemplate.warehouse.entity.WareInfoEntity;
import com.ecommercesystemtemplate.warehouse.feign.MemberFeignService;
import com.ecommercesystemtemplate.warehouse.service.WareInfoService;
import com.ecommercesystemtemplate.warehouse.vo.MemberAddressVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;


@Service("wareInfoService")
public class WareInfoServiceImpl extends ServiceImpl<WareInfoDao, WareInfoEntity> implements WareInfoService {

    final
    MemberFeignService memberFeignService;

    public WareInfoServiceImpl(MemberFeignService memberFeignService) {
        this.memberFeignService = memberFeignService;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key = (String) params.get("key");
        QueryWrapper<WareInfoEntity> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(key)){
            wrapper.eq("id",key).or().like("name",key).or().like("address",key).or().like("areacode",key);
        }
        IPage<WareInfoEntity> page = this.page(
                new Query<WareInfoEntity>().getPage(params),wrapper
        );

        return new PageUtils(page);
    }

    @Override
    public BigDecimal getFreight(Long addrId) {
        R r = memberFeignService.addrInfo(addrId);
        MemberAddressVo data = r.getData("memberReceiveAddress",new TypeReference<MemberAddressVo>() {
        });
        if (data != null){
            String phone = data.getPhone();
            // an easy logic to calculate freight
            String substring = phone.substring(phone.length() - 1, phone.length());
            return new BigDecimal(substring);
        }
        return new BigDecimal(0);
    }

}
