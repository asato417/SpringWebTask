package com.example.springwebtask.service;

import com.example.springwebtask.entity.CategoryRecord;
import com.example.springwebtask.repository.ICategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService implements ICategoryService{
    @Autowired
    ICategoryRepository categoryRepository;

    @Override
    public List<CategoryRecord> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public CategoryRecord findById(int id) {
        return categoryRepository.findById(id);
    }

    @Override
    public CategoryRecord findByName(String name) {
        return categoryRepository.findByName(name);
    }
}
