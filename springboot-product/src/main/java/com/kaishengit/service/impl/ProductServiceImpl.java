package com.kaishengit.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kaishengit.entity.Product;
import com.kaishengit.entity.ProductExample;
import com.kaishengit.mapper.ProductMapper;
import com.kaishengit.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public PageInfo<Product> findProductByPageNo(Integer pageNo) {
        PageHelper.startPage(pageNo,10);
        return new PageInfo<>(productMapper.selectByExample(new ProductExample()));
    }

    @Override
    public void saveProduct(Product product) {
        productMapper.insertSelective(product);

        redisTemplate.opsForValue().set("product:" + product.getId(),JSON.toJSONString(product));

        for(int i = 1; i <= product.getProductInventory(); i++) {
            redisTemplate.opsForList().leftPush("product:" + product.getId() + " :inventory",String.valueOf(i));
        }

        //添加动态定时任务，秒杀结束后同步库存到数据库


    }

    @Override
    public Product findProductById(Integer id) {
        return productMapper.selectByPrimaryKey(id);
    }

    @Override
    public void updateProduct(Product product) {
        productMapper.updateByPrimaryKeySelective(product);
    }

    @Override
    public void delProductById(Integer id) {
        productMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void buyProduct(Integer id) throws RuntimeException {
        String json = redisTemplate.opsForValue().get("product:" + id);

        Product product = JSON.parseObject(json,Product.class);

        if(product.isStart()) {
            throw new RuntimeException("来早了");
        }
        if(product.isEnd()) {
            throw new RuntimeException("来晚了");
        }

        //预防超卖！！！
        if(redisTemplate.opsForList().leftPop("product:" + id + " :inventory") != null) {
            Long size = redisTemplate.opsForList().size("product:" + id + ":inventory");
            System.out.println("抢购成功" + size);
            //订单 --> MQ --> 数据库

            //秒杀结束后，同步redis中的库存到数据库，用Quartz创建动态任务
        } else {
            System.out.println("已售罄");
        }

    }
}
