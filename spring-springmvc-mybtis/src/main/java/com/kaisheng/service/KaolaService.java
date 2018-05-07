package com.kaisheng.service;

import com.github.pagehelper.PageInfo;
import com.kaisheng.entity.Kaola;
import com.kaisheng.entity.KaolaType;

import java.util.List;
import java.util.Map;

public interface KaolaService {
     /**
      * @date 2018/4/10
      * @param id 主键值
      * @return Kaola对象
      */
    Kaola findById(Integer id);

    /**
     * 根据页码查找商品列表
     * @param pageNo
     * @return
     */
    PageInfo<Kaola> findAllByPageNo(Integer pageNo);

    /**
     * 查找所有商品的类型
     * @return
     */
    List<KaolaType> findAllType();

    /**新增商品
     * @param kaola
     */
    void saveKaola(Kaola kaola);


    /**
     * 根据id删除商品
     * @param id
     */
    void deleteKaolaById(Integer id);


    /**
     * 更新商品信息
     * @param kaola
     */
    void updateKaola(Kaola kaola);

    /**
     * 根据当前页码和参数查询商品
     * @param pageNo 当前页码
     * @param map 参数map
     * @return
     */
    PageInfo<Kaola> findAllByPageNoAndParam(Integer pageNo, Map<String, Object> map);
}
