package com.example.springwebtask.service;

import com.example.springwebtask.entity.ProductRecord;
import com.example.springwebtask.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService implements IProductService{
    @Autowired
    private IProductRepository productRepository;

    @Override
    public List<ProductRecord> findAll() {
        return productRepository.findAll();
    }

    @Override
    public List<ProductRecord> findByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public ProductRecord findByProductId(String productId) {
        return productRepository.findByProductId(productId);
    }

    @Override
    public ProductRecord findById(int id) {
        return productRepository.findBytId(id);
    }

    @Override
    public int insert(ProductRecord product) {
        return productRepository.insert(product);
    }

    @Override
    public int delete(String productId) {
        return productRepository.delete(productId);
    }

    @Override
    public int update(ProductRecord product) {
        return productRepository.update(product);
    }

//    @Override
//    public int insert(String productId, int categoryId, String name, int price, String description) {
//        return productRepository.insert(productId, categoryId, name, price, description);
//    }
}
