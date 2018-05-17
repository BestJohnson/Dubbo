package com.kaishengit.service;

import com.github.pagehelper.PageInfo;
import com.kaishengit.entity.Product;

public interface ProductService {


    PageInfo<Product> findProductByPageNo(Integer pageNo);

    void saveProduct(Product product);

    Product findProductById(Integer id);

    void updateProduct(Product product);

    void delProductById(Integer id);

    void buyProduct(Integer id) throws RuntimeException;
}
