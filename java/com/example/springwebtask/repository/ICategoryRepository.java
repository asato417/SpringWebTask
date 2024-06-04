package com.example.springwebtask.repository;

import com.example.springwebtask.entity.CategoryRecord;

import java.util.List;

public interface ICategoryRepository {
    List<CategoryRecord> findAll();
    CategoryRecord findById(int id);
    CategoryRecord findByName(String name);

}
