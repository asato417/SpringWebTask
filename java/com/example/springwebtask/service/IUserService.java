package com.example.springwebtask.service;

import com.example.springwebtask.entity.UserRecord;

public interface IUserService {
    UserRecord findUser(String loginId, String pass);
}
