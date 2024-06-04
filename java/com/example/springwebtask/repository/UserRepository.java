package com.example.springwebtask.repository;

import com.example.springwebtask.entity.UserRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository implements IUserRepository{
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public UserRecord findUser(String loginId, String pass) {
        var param = new MapSqlParameterSource();
        param.addValue("loginId", loginId);
        param.addValue("pass", pass);
        final String FIND_USER_SQL = "SELECT * FROM users WHERE login_id = :loginId AND password = :pass";
        var resultList = jdbcTemplate.query(FIND_USER_SQL, param,
                new DataClassRowMapper<>(UserRecord.class));
        return resultList.isEmpty() ? null : resultList.get(0);
    }
}
