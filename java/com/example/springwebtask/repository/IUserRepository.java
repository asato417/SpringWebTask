package com.example.springwebtask.repository;

import com.example.springwebtask.entity.UserRecord;
import org.apache.catalina.User;

import java.util.List;

public interface IUserRepository {
    UserRecord findUser(String loginId, String pass);
}
