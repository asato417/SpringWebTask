package com.example.springwebtask.service;

import com.example.springwebtask.entity.CategoryRecord;

import java.util.List;

public interface ICategoryService {
    List<CategoryRecord> findAll();
    CategoryRecord findById(int id);
    CategoryRecord findByName(String name);
}
