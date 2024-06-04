package com.example.springwebtask.service;

import com.example.springwebtask.entity.ProductRecord;

import java.util.List;

public interface IProductService {
    List<ProductRecord> findAll();
    List<ProductRecord> findByName(String name);
    ProductRecord findByProductId(String productId);
    ProductRecord findById(int id);
//    int insert(String productId, int categoryId, String name, int price, String description);
    int insert(ProductRecord product);
    int delete(String productId);
    int update(ProductRecord product);

}
