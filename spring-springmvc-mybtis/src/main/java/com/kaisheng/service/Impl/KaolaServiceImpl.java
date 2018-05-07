package com.kaisheng.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.kaisheng.entity.Kaola;
import com.kaisheng.entity.KaolaType;
import com.kaisheng.mapper.KaolaMapper;
import com.kaisheng.mapper.KaolaTypeMapper;
import com.kaisheng.service.KaolaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Map;

@Service
//@CacheConfig(cacheNames = "kaola")
public class KaolaServiceImpl implements KaolaService{
    private Logger logger = LoggerFactory.getLogger(KaolaServiceImpl.class);

    @Autowired
    private KaolaMapper kaolaMapper;
    @Autowired
    private KaolaTypeMapper kaolaTypeMapper;
    //@Autowired
    //private JedisPool jedisPool;

    /**
     * @param id 主键值
     * @return Kaola对象
     * @date 2018/4/10
     */
    @Override
    @Cacheable(cacheNames = "kaola")
    public Kaola findById(Integer id) {

        /*Jedis jedis = jedisPool.getResource();

        final String KEY = "kaola" + id;

        Kaola kaola = null;
        if(jedis.exists(KEY)) {
            String value = jedis.get(KEY);
            kaola = new Gson().fromJson(value,Kaola.class);
        } else {
            //不存在，则去数据库查，并存入redis
            kaola = kaolaMapper.findById(id);

            if(kaola != null) {
                String json = new Gson().toJson(kaola);
                jedis.set(KEY,json);
            }

        }
        jedis.close();*/


       return kaolaMapper.findById(id);
    }

    /**
     * 根据页码查找商品列表
     * @param pageNo
     * @return
     */
    @Override
    public PageInfo<Kaola> findAllByPageNo(Integer pageNo) {
        PageHelper.startPage(pageNo,10);
        List<Kaola> kaolaList = kaolaMapper.findAllWithType();
        return new PageInfo<>(kaolaList);
    }

    /**
     * 查找所有商品的类型
     * @return
     */
    @Override
    public List<KaolaType> findAllType() {
        return kaolaTypeMapper.findAll();
    }

    /**
     * 新增商品
     * @param kaola
     */
    @Override
    public void saveKaola(Kaola kaola) {
        kaola.setCommentNum(Kaola.DEFAULT_COMMENT_NUM);
        kaolaMapper.save(kaola);
        logger.info("新增商品 {}",kaola);
    }

    /**
     * 根据id删除商品
     * @param id
     */
    @Override
    public void deleteKaolaById(Integer id) {
        Kaola kaola = kaolaMapper.findById(id);
        if(kaola != null) {
            kaolaMapper.deleteById(id);
            logger.info("删除商品 {}",kaola);
        }
    }

    /**
     * 更新商品信息
     * @param kaola
     */
    @Override
    public void updateKaola(Kaola kaola) {
        kaolaMapper.update(kaola);
        logger.info("商品修改为 {}",kaola);
    }

    @Override
    public PageInfo<Kaola> findAllByPageNoAndParam(Integer pageNo, Map<String, Object> map) {
        PageHelper.startPage(pageNo,10);
        List<Kaola> kaolaList = kaolaMapper.findAllByParam(map);
        return new PageInfo<>(kaolaList);
    }


}
