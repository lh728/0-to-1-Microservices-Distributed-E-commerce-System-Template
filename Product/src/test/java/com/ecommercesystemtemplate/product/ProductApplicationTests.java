package com.ecommercesystemtemplate.product;

import com.ecommercesystemtemplate.product.entity.BrandEntity;
import com.ecommercesystemtemplate.product.service.BrandService;
import com.ecommercesystemtemplate.product.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@SpringBootTest
@Slf4j
class ProductApplicationTests {
    @Autowired
    BrandService brandService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedissonClient redissonClient;

    @Test
    void testRedisson() {
        System.out.println(redissonClient);
    }

    @Test
    void contextLoads() {
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setName("aaa");
        brandEntity.setDescript("");
        brandService.save(brandEntity);
        System.out.printf("save success");
    }

    @Test
    void testFindPath() {
        Long[] path = categoryService.findCatelogPath(225L);
        log.info("path: {}", Arrays.asList(path));
        assertArrayEquals(new Long[]{2L, 34L, 225L}, path);
    }

    @Test
    void testRedis() {
        stringRedisTemplate.opsForValue().set("hello", "world");
        System.out.println(stringRedisTemplate.opsForValue().get("hello"));
    }



}
