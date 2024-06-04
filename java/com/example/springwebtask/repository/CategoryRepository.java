package com.example.springwebtask.repository;

import com.example.springwebtask.entity.CategoryRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CategoryRepository implements ICategoryRepository{
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<CategoryRecord> findAll() {
        return jdbcTemplate.query("SELECT * FROM categories ORDER BY id",
                new DataClassRowMapper<>(CategoryRecord.class));
    }

    @Override
    public CategoryRecord findById(int id) {
        final String FIND_CATEGORY_NAME_SQL = "SELECT * FROM categories WHERE id = :id";

        var param = new MapSqlParameterSource();
        param.addValue("id", id);

        var resultList = jdbcTemplate.query(FIND_CATEGORY_NAME_SQL, param,
                new DataClassRowMapper<>(CategoryRecord.class));

        return resultList.isEmpty()? null : resultList.get(0);

    }

    @Override
    public CategoryRecord findByName(String name) {
        final String SQL = "SELECT * FROM categories WHERE name = :name";

        var param = new MapSqlParameterSource();
        param.addValue("name", name);

        var resultList = jdbcTemplate.query(SQL, param,
                new DataClassRowMapper<>(CategoryRecord.class));

        return resultList.isEmpty()? null : resultList.get(0);
    }
}