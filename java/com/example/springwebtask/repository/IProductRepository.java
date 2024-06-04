package com.example.springwebtask.repository;

import com.example.springwebtask.entity.ProductRecord;

import java.util.List;

public interface IProductRepository {
    List<ProductRecord> findAll();
    List<ProductRecord> findByName(String name);
    ProductRecord findByProductId(String productId);
    ProductRecord findBytId(int id);
//    int insert(String productId, int categoryId, String name, int price, String description);
    int insert(ProductRecord product);
    int delete(String productId);
    int update(ProductRecord product);
}
