package com.ecommercesystemtemplate.order.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ecommercesystemtemplate.common.exception.NoStockException;
import com.ecommercesystemtemplate.common.to.mq.OrderTo;
import com.ecommercesystemtemplate.common.to.mq.QuickFlashSaleOrderTo;
import com.ecommercesystemtemplate.common.utils.PageUtils;
import com.ecommercesystemtemplate.common.utils.Query;
import com.ecommercesystemtemplate.common.utils.R;
import com.ecommercesystemtemplate.common.vo.MemberResponseVo;
import com.ecommercesystemtemplate.order.constant.OrderConstant;
import com.ecommercesystemtemplate.order.dao.OrderDao;
import com.ecommercesystemtemplate.order.entity.OrderEntity;
import com.ecommercesystemtemplate.order.entity.OrderItemEntity;
import com.ecommercesystemtemplate.order.entity.PaymentInfoEntity;
import com.ecommercesystemtemplate.order.enume.OrderStatusEnum;
import com.ecommercesystemtemplate.order.feign.CartFeignService;
import com.ecommercesystemtemplate.order.feign.MemberFeignService;
import com.ecommercesystemtemplate.order.feign.ProductFeignService;
import com.ecommercesystemtemplate.order.feign.WmsFeignService;
import com.ecommercesystemtemplate.order.interceptor.LoginUserInterceptor;
import com.ecommercesystemtemplate.order.service.OrderItemService;
import com.ecommercesystemtemplate.order.service.OrderService;
import com.ecommercesystemtemplate.order.service.PaymentInfoService;
import com.ecommercesystemtemplate.order.to.OrderCreateTo;
import com.ecommercesystemtemplate.order.vo.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service("orderService")
public class OrderServiceImpl extends ServiceImpl<OrderDao, OrderEntity> implements OrderService {

    private ThreadLocal<OrderSubmitVo> threadLocal = new ThreadLocal<>();
    final ProductFeignService productFeignService;
    final MemberFeignService memberFeignService;
    final CartFeignService cartFeignService;
    final
    ThreadPoolExecutor threadPoolExecutor;
    final WmsFeignService wmsFeignService;
    final StringRedisTemplate stringRedisTemplate;
    final OrderItemService orderItemService;
    final RabbitTemplate rabbitTemplate;
    final PaymentInfoService paymentInfoService;


    public OrderServiceImpl(ProductFeignService productFeignService, MemberFeignService memberFeignService, CartFeignService cartFeignService, ThreadPoolExecutor threadPoolExecutor, WmsFeignService wmsFeignService, StringRedisTemplate stringRedisTemplate, OrderItemService orderItemService, RabbitTemplate rabbitTemplate, PaymentInfoService paymentInfoService) {
        this.productFeignService = productFeignService;
        this.memberFeignService = memberFeignService;
        this.cartFeignService = cartFeignService;
        this.threadPoolExecutor = threadPoolExecutor;
        this.wmsFeignService = wmsFeignService;
        this.stringRedisTemplate = stringRedisTemplate;
        this.orderItemService = orderItemService;
        this.rabbitTemplate = rabbitTemplate;
        this.paymentInfoService = paymentInfoService;
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
    public OrderConfirmVo confirmOrder() throws ExecutionException, InterruptedException {
        OrderConfirmVo orderConfirmVo = new OrderConfirmVo();
        MemberResponseVo memberResponseVo = LoginUserInterceptor.loginUser.get();
        // share interceptor data to async task
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        CompletableFuture<Void> getAddressFuture = CompletableFuture.runAsync(() -> {
            // 1. remote search all delivery addresses list
            RequestContextHolder.setRequestAttributes(requestAttributes);
            List<MemberAddressVo> address = memberFeignService.getAddress(memberResponseVo.getId());
            orderConfirmVo.setAddress(address);

        }, threadPoolExecutor);

        CompletableFuture<Void> cartItemsFuture = CompletableFuture.runAsync(() -> {
            // 2. remote search all payment list in cart
            RequestContextHolder.setRequestAttributes(requestAttributes);
            List<OrderItemVo> currentUserCartItems = cartFeignService.getCurrentUserCartItems();
            orderConfirmVo.setOrderItemVoList(currentUserCartItems);
        },threadPoolExecutor).thenRunAsync(() -> {
            List<OrderItemVo> orderItemVoList = orderConfirmVo.getOrderItemVoList();
            List<Long> list = orderItemVoList.stream().map(OrderItemVo::getSkuId).toList();
            // 3. remote search all stock list
            RequestContextHolder.setRequestAttributes(requestAttributes);
            R skusHaveStock = wmsFeignService.getSkusHaveStock(list);
            List<SkuStockVo> data = skusHaveStock.getData(new TypeReference<List<SkuStockVo>>() {
            });
            if (data != null){
                Map<Long, Boolean> collect = data.stream().collect(Collectors.toMap(SkuStockVo::getSkuId, SkuStockVo::getHasStock));
                orderConfirmVo.setStocks(collect);
            }

        },threadPoolExecutor);

        // 3. get user points
        Integer integration = memberResponseVo.getIntegration();
        orderConfirmVo.setPoints(integration);

        // 4. pay idempotence (anti-resubmit token)
        String token = UUID.randomUUID().toString().replace("-", "");
        stringRedisTemplate.opsForValue().set(OrderConstant.USER_ORDER_TOKEN_PREFIX+memberResponseVo.getId(),token,30, TimeUnit.MINUTES);
        orderConfirmVo.setOrderToken(token);
        CompletableFuture.allOf(getAddressFuture, cartItemsFuture).get();

        return orderConfirmVo;


    }

    @Override
    @Transactional
//    @GlobalTransactional
    public SubmitOrderResponseVo submitOrder(OrderSubmitVo vo) {
        threadLocal.set(vo);
        SubmitOrderResponseVo submitOrderResponseVo = new SubmitOrderResponseVo();
        MemberResponseVo memberResponseVo = LoginUserInterceptor.loginUser.get();
        submitOrderResponseVo.setStatusCode(0);
        // 1. check order token
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        String orderToken = vo.getOrderToken();
        Long result = stringRedisTemplate.execute(
                new DefaultRedisScript<>(script, Long.class),
                Collections.singletonList(OrderConstant.USER_ORDER_TOKEN_PREFIX + memberResponseVo.getId()),
                orderToken);
        if (result == 0L){
            //fail
            submitOrderResponseVo.setStatusCode(1);
            return submitOrderResponseVo;
        }else{
            //success
            // 1. create order
            OrderCreateTo order = createOrder();
            // 2. check price
            BigDecimal payAmount = order.getOrder().getPayAmount();
            BigDecimal payPrice = vo.getPayPrice();
            if (Math.abs(payAmount.subtract(payPrice).doubleValue()) < 0.01){
                // 3. save order
                saveOrder(order);
                // 4. lock stock info
                WareSkuLockVo wareSkuLockVo = new WareSkuLockVo();
                wareSkuLockVo.setOrderSn(order.getOrder().getOrderSn());
                List<OrderItemVo> list = order.getOrderItems().stream().map(item -> {
                    OrderItemVo orderItemVo = new OrderItemVo();
                    orderItemVo.setSkuId(item.getSkuId());
                    orderItemVo.setCount(item.getSkuQuantity());
                    orderItemVo.setTitle(item.getSkuName());
                    return orderItemVo;
                }).toList();
                wareSkuLockVo.setOrderItemVoList(list);
                R r = wmsFeignService.lockOrderStock(wareSkuLockVo);
                if (r.getCode() == 0){
                    // success
                    submitOrderResponseVo.setOrder(order.getOrder());
                    // create order then send to mq
                    rabbitTemplate.convertAndSend("order-event-exchange", "order.create.order", order.getOrder());
                    return submitOrderResponseVo;
                }else{
//                    submitOrderResponseVo.setStatusCode(3);
                    String msg = (String) r.get("msg");
                    throw new NoStockException(msg);
//                    return submitOrderResponseVo;
                }
            } else{
                // 5. return fail
                submitOrderResponseVo.setStatusCode(2);
                return submitOrderResponseVo;
            }
        }


    }

    @Override
    public OrderEntity getOrderStatus(String orderSn) {
        OrderEntity order_sn = this.getOne(new QueryWrapper<OrderEntity>().eq("order_sn", orderSn));
        return order_sn;
    }

    @Override
    public void closeOrder(OrderEntity orderEntity) {
        // 1. query order's current status
        OrderEntity byId = this.getById(orderEntity.getId());
        if (byId.getStatus().equals(OrderStatusEnum.CREATE_NEW.getCode())){
            // 2. close order
            OrderEntity update = new OrderEntity();
            update.setId(orderEntity.getId());
            update.setStatus(OrderStatusEnum.CANCLED.getCode());
            this.updateById(update);
            // 3. send message to mq, unlock stock
            OrderTo orderTo = new OrderTo();
            BeanUtils.copyProperties(orderEntity,orderTo);
            rabbitTemplate.convertAndSend("order-event-exchange", "order.release.other", orderTo);
        }
    }

    @Override
    public PayVo getOrderPayInfo(String orderSn) {
        PayVo payVo = new PayVo();
        OrderEntity order = this.getOrderStatus(orderSn);
        BigDecimal bigDecimal = order.getPayAmount().setScale(2, BigDecimal.ROUND_UP);
        payVo.setTotal_amount(bigDecimal.toString());
        payVo.setOut_trade_no(orderSn);

        List<OrderItemEntity> orderSns = orderItemService.list(new QueryWrapper<OrderItemEntity>().eq("order_sn", orderSn));
        OrderItemEntity entity = orderSns.get(0);
        payVo.setSubject(entity.getSkuName());
        payVo.setBody(entity.getSkuAttrsVals());

        return payVo;
    }

    @Override
    public PageUtils queryPageWithItems(Map<String, Object> params) {
        MemberResponseVo memberResponseVo = LoginUserInterceptor.loginUser.get();

        IPage<OrderEntity> page = this.page(
                new Query<OrderEntity>().getPage(params),
                new QueryWrapper<OrderEntity>().eq("member_id", memberResponseVo.getId()).orderByDesc("id")
        );

        List<OrderEntity> orderSn = page.getRecords().stream().map(orderEntity -> {
            orderEntity.setItems(orderItemService.list(new QueryWrapper<OrderItemEntity>().eq("order_sn", orderEntity.getOrderSn())));
            return orderEntity;
        }).toList();
        page.setRecords(orderSn);

        return new PageUtils(page);
    }

    /**
     * handle Alipay pay result
     * @param payAsyncVo
     * @return
     */
    @Override
    @Transactional
    public String handlePayResult(PayAsyncVo payAsyncVo) {
        // 1. save order statement
        PaymentInfoEntity paymentInfoEntity = new PaymentInfoEntity();
        paymentInfoEntity.setAlipayTradeNo(payAsyncVo.getTrade_no());
        paymentInfoEntity.setOrderSn(payAsyncVo.getOut_trade_no());
        paymentInfoEntity.setPaymentStatus(payAsyncVo.getTrade_status());
        paymentInfoEntity.setCallbackTime(payAsyncVo.getNotify_time());
        paymentInfoService.save(paymentInfoEntity);

        // 2. update order status
        if (payAsyncVo.getTrade_status().equals("TRADE_SUCCESS") || payAsyncVo.getTrade_status().equals("TRADE_FINISHED")){
            String outTradeNo = payAsyncVo.getOut_trade_no();
            this.baseMapper.updateOrderStatus(outTradeNo, OrderStatusEnum.PAID.getCode());
        }
        return "success";
    }

    @Override
    public void createFlashSaleOrder(QuickFlashSaleOrderTo orderEntity) {
        // 1. save order info
        OrderEntity entity = new OrderEntity();
        entity.setOrderSn(orderEntity.getOrderSn());
        entity.setMemberId(orderEntity.getMemberId());
        entity.setStatus(OrderStatusEnum.CREATE_NEW.getCode());
        BigDecimal multiply = orderEntity.getSeckillPrice().multiply(new BigDecimal("" + orderEntity.getNum()));
        entity.setPayAmount(multiply);
        this.save(entity);
        // 2. save order item
        OrderItemEntity itemEntity = new OrderItemEntity();
        itemEntity.setOrderSn(orderEntity.getOrderSn());
        itemEntity.setRealAmount(multiply);
        itemEntity.setSkuQuantity(orderEntity.getNum());
        orderItemService.save(itemEntity);

    }

    private void saveOrder(OrderCreateTo order) {
        OrderEntity orderEntity = order.getOrder();
        orderEntity.setModifyTime(new Date());
        this.save(orderEntity);

        List<OrderItemEntity> orderItems = order.getOrderItems();
        orderItemService.saveBatch(orderItems);
    }

    private OrderCreateTo createOrder(){
        OrderCreateTo orderCreateTo = new OrderCreateTo();
        // 1. generate order id
        String orderSn = IdWorker.getTimeId();
        // 2. set address and freight
        OrderEntity orderEntity = buildOrder(orderSn);
        // 3. get all order items
        List<OrderItemEntity> orderItemEntities = buildOrderItems(orderSn);
        // 4. compute price
        computePrice(orderEntity, orderItemEntities);
        orderCreateTo.setOrder(orderEntity);
        orderCreateTo.setOrderItems(orderItemEntities);

        return orderCreateTo;

    }

    private void computePrice(OrderEntity orderEntity, List<OrderItemEntity> orderItemEntities) {
        BigDecimal total = new BigDecimal(0);
        BigDecimal coupon = new BigDecimal(0);
        BigDecimal point = new BigDecimal(0);
        BigDecimal promotion = new BigDecimal(0);
        BigDecimal gift = new BigDecimal(0);
        BigDecimal growth = new BigDecimal(0);
        for (OrderItemEntity item : orderItemEntities) {
            coupon = coupon.add(item.getCouponAmount());
            point = point.add(item.getIntegrationAmount());
            promotion = promotion.add(item.getPromotionAmount());
            total = total.add(item.getRealAmount());
            gift = gift.add(new BigDecimal(item.getGiftIntegration().toString()));
            growth = growth.add(new BigDecimal(item.getGiftGrowth().toString()));
        }
        // 1. relate to order price
        orderEntity.setTotalAmount(total);
        orderEntity.setPayAmount(total.add(orderEntity.getFreightAmount()));
        orderEntity.setCouponAmount(coupon);
        orderEntity.setIntegrationAmount(point);
        orderEntity.setPromotionAmount(promotion);

        // 2. relate to gift point and member growth point
        orderEntity.setIntegration(gift.intValue());
        orderEntity.setGrowth(growth.intValue());

        // 3. relate to others
        orderEntity.setDeleteStatus(0);

    }

    private OrderEntity buildOrder(String orderId) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setOrderSn(orderId);
        orderEntity.setMemberId(LoginUserInterceptor.loginUser.get().getId());
        OrderSubmitVo orderSubmitVo = threadLocal.get();
        R freight = wmsFeignService.getFreight(orderSubmitVo.getAddrId());
        FreightVo data = freight.getData(new TypeReference<FreightVo>() {
        });
        BigDecimal freight1 = data.getFreight();
        orderEntity.setFreightAmount(freight1);
        orderEntity.setReceiverCity(data.getAddress().getCity());
        orderEntity.setReceiverDetailAddress(data.getAddress().getDetailAddress());
        orderEntity.setReceiverName(data.getAddress().getName());
        orderEntity.setReceiverPhone(data.getAddress().getPhone());
        orderEntity.setReceiverProvince(data.getAddress().getPostCode());
        orderEntity.setReceiverPostCode(data.getAddress().getProvince());
        orderEntity.setReceiverRegion(data.getAddress().getRegion());

        orderEntity.setStatus(OrderStatusEnum.CREATE_NEW.getCode());
        orderEntity.setAutoConfirmDay(7);
        return orderEntity;
    }

    private List<OrderItemEntity> buildOrderItems(String orderSn) {
        // check item price finally
        List<OrderItemVo> currentUserCartItems = cartFeignService.getCurrentUserCartItems();
        if (currentUserCartItems != null && !currentUserCartItems.isEmpty()){
            List<OrderItemEntity> list = currentUserCartItems.stream().map(item -> {
                OrderItemEntity entity = buildOrderItem(item);
                entity.setOrderSn(orderSn);
                return entity;
            }).toList();
            return list;
        }
        return null;

    }

    private OrderItemEntity buildOrderItem(OrderItemVo item) {
        OrderItemEntity entity = new OrderItemEntity();
        // 1. order sn
        // 2. product spu info
        Long skuId = item.getSkuId();
        R r = productFeignService.getSpuInfoBySkuId(skuId);
        SpuInfoVo data = r.getData(new TypeReference<SpuInfoVo>() {
        });
        entity.setSpuId(data.getId());
        entity.setSpuName(data.getSpuName());
        entity.setCategoryId(data.getCatalogId());
        entity.setSpuBrand(data.getBrandId().toString());

        // 3. product sku info
        entity.setSkuId(item.getSkuId());
        entity.setSkuName(item.getTitle());
        entity.setSkuPic(item.getImage());
        entity.setSkuPrice(item.getPrice());
        entity.setSkuAttrsVals(StringUtils.collectionToDelimitedString(item.getSkuAttr(), ";"));
        entity.setSkuQuantity(item.getCount());

        // 4. point info
        entity.setGiftGrowth(item.getPrice().multiply(new BigDecimal(item.getCount().toString())).intValue());
        entity.setGiftIntegration(item.getPrice().multiply(new BigDecimal(item.getCount().toString())).intValue());

        // 5. order price info
        entity.setPromotionAmount(BigDecimal.ZERO);
        entity.setCouponAmount(BigDecimal.ZERO);
        entity.setIntegrationAmount(BigDecimal.ZERO);
        BigDecimal origin = entity.getSkuPrice().multiply(new BigDecimal(entity.getSkuQuantity().toString()));
        BigDecimal subtract = origin.subtract(entity.getCouponAmount()).subtract(entity.getPromotionAmount()).subtract(entity.getIntegrationAmount());
        entity.setRealAmount(subtract);

        return entity;

    }

}
