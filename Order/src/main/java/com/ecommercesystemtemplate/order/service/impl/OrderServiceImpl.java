package com.ecommercesystemtemplate.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecommercesystemtemplate.common.utils.PageUtils;
import com.ecommercesystemtemplate.common.utils.Query;
import com.ecommercesystemtemplate.common.vo.MemberResponseVo;
import com.ecommercesystemtemplate.order.dao.OrderDao;
import com.ecommercesystemtemplate.order.entity.OrderEntity;
import com.ecommercesystemtemplate.order.feign.CartFeignService;
import com.ecommercesystemtemplate.order.feign.MemberFeignService;
import com.ecommercesystemtemplate.order.interceptor.LoginUserInterceptor;
import com.ecommercesystemtemplate.order.service.OrderService;
import com.ecommercesystemtemplate.order.vo.MemberAddressVo;
import com.ecommercesystemtemplate.order.vo.OrderConfirmVo;
import com.ecommercesystemtemplate.order.vo.OrderItemVo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {

    final
    MemberFeignService memberFeignService;

    final
    CartFeignService cartFeignService;

    public OrderServiceImpl(MemberFeignService memberFeignService, CartFeignService cartFeignService) {
        this.memberFeignService = memberFeignService;
        this.cartFeignService = cartFeignService;
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<OrderEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public OrderConfirmVo confirmOrder() {
        OrderConfirmVo orderConfirmVo = new OrderConfirmVo();
        MemberResponseVo memberResponseVo = LoginUserInterceptor.loginUser.get();

        // 1. remote search all delivery addresses list
        List<MemberAddressVo> address = memberFeignService.getAddress(memberResponseVo.getId());
        orderConfirmVo.setAddress(address);

        // 2. remote search all payment list in cart
        List<OrderItemVo> currentUserCartItems = cartFeignService.getCurrentUserCartItems();
        orderConfirmVo.setOrderItemVoList(currentUserCartItems);

        // 3. get user points
        Integer integration = memberResponseVo.getIntegration();
        orderConfirmVo.setPoints(integration);

        return orderConfirmVo;


    }

}
