package com.example.springwebtask.repository;

import com.example.springwebtask.entity.ProductRecord;
import com.example.springwebtask.entity.UserRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepository implements IProductRepository{
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;
    @Override
    public List<ProductRecord> findAll() {
        return jdbcTemplate.query("SELECT * FROM products ORDER BY product_id",
                new DataClassRowMapper<>(ProductRecord.class));
    }

    @Override
    public List<ProductRecord> findAllSort(String columnName, String order) {
        var param = new MapSqlParameterSource();
        param.addValue("columnName", "category_id");
        var sql = "SELECT * FROM products ORDER BY " + columnName  + " " + order;
        return jdbcTemplate.query(sql, new DataClassRowMapper<>(ProductRecord.class));
    }

    @Override
    public List<ProductRecord> findByName(String name) {
        var param = new MapSqlParameterSource();
        name = "%" + name + "%";
        param.addValue("name", name);
        final String FIND_PRODUCT_SQL = "SELECT * FROM products WHERE name LIKE :name OR category_id IN (SELECT id FROM categories WHERE name LIKE :name )";
        var resultList = jdbcTemplate.query(FIND_PRODUCT_SQL, param,
                new DataClassRowMapper<>(ProductRecord.class));
        return resultList.isEmpty() ? null : resultList;
    }

    @Override
    public List<ProductRecord> findByNameSort(List<String> keys, String columnName, String order) {
//        var param = new MapSqlParameterSource();
//        final String FIND_PRODUCT_SQL = "SELECT * FROM products WHERE name LIKE :name OR category_id IN (SELECT id FROM categories WHERE name LIKE :name ) ORDER BY " + columnName + " " + order;
//        var sql = "SELECT * FROM products WHERE name LIKE '%" + keys.get(0) + "%'";
//        var sql2 = "OR category_id IN (SELECT id FROM categories WHERE name LIKE '%" + keys.get(0) + "%'";
        var sql = "SELECT * FROM products p JOIN categories c ON p.category_id = c.id WHERE p.name || c.name LIKE '%" + keys.get(0) + "%'";
        for (var i=1; i<keys.size(); i++){
//            sql += " AND name LIKE '%" + keys.get(i) + "%'";
            sql += " AND p.name || c.name LIKE '%" + keys.get(i) + "%'";
//            sql2 += " AND name LIKE '%" + keys.get(i) + "%'";
        }
        sql += " ORDER BY " + columnName + " " + order;
        System.out.println(sql);
        var resultList = jdbcTemplate.query(sql,
                new DataClassRowMapper<>(ProductRecord.class));
        return resultList.isEmpty() ? null : resultList;
    }

    @Override
    public ProductRecord findByProductId(String productId) {
        var param = new MapSqlParameterSource();
        param.addValue("productId", productId);
        final String FIND_PRODUCT_SQL = "SELECT * FROM products WHERE product_id = :productId";
        var resultList = jdbcTemplate.query(FIND_PRODUCT_SQL, param,
                new DataClassRowMapper<>(ProductRecord.class));
        return resultList.isEmpty() ? null : resultList.get(0);
    }

    @Override
    public ProductRecord findBytId(int id) {
        var param = new MapSqlParameterSource();
        param.addValue("id", id);
        final String FIND_PRODUCT_SQL = "SELECT * FROM products WHERE id = :id";
        var resultList = jdbcTemplate.query(FIND_PRODUCT_SQL, param,
                new DataClassRowMapper<>(ProductRecord.class));

        return resultList.isEmpty() ? null : resultList.get(0);
    }

    @Override
    public int insert(ProductRecord product) {
        var param = new MapSqlParameterSource();
        param.addValue("pId", product.productId());
        param.addValue("cId", product.categoryId());
        param.addValue("name", product.name());
        param.addValue("price", product.price());
        param.addValue("description", product.description());
        param.addValue("imgPath", product.imagePath());
        final String INSERT =
                "INSERT INTO products (product_id, category_id, name, price, description, image_path) VALUES(:pId, :cId, :name, :price, :description, :imgPath)";
        return jdbcTemplate.update(INSERT, param);
    }

    @Override
    public int delete(String productId) {
        var param = new MapSqlParameterSource();
        param.addValue("productId", productId);
        final String DELETE = "DELETE FROM products WHERE product_id = :productId";
        return jdbcTemplate.update(DELETE, param);
    }

    @Override
    public int update(ProductRecord product) {
        String update = "";
        var param = new MapSqlParameterSource();
        param.addValue("productId", product.productId());
        param.addValue("name", product.name());
        param.addValue("price", product.price());
        param.addValue("categoryId", product.categoryId());
        param.addValue("description", product.description());
        param.addValue("id", product.id());
        if(!product.imagePath().equals("")){
            param.addValue("file", product.imagePath());
            update = "UPDATE products SET product_id = :productId, name=:name, price=:price, category_id=:categoryId, description=:description, image_path=:file WHERE id = :id";
        } else {
            update = "UPDATE products SET product_id = :productId, name=:name, price=:price, category_id=:categoryId, description=:description WHERE id = :id";

        }
        return jdbcTemplate.update(update, param);

    }

//    @Override
//    public int insert(String productId, int categoryId, String name, int price, String description) {
//        var param = new MapSqlParameterSource();
//        param.addValue("pId", productId);
//        param.addValue("cId", categoryId);
//        param.addValue("name", name);
//        param.addValue("price", price);
//        param.addValue("description", description);
//        final String INSERT = "INSERT INTO products (product_id, category_id, name, price, description" +
//                "VALUE(:pID, :cID, :name, :price, :description)";
//        return jdbcTemplate.update(INSERT, param);
//    }
}
