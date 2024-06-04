package com.example.springwebtask.service;

import com.example.springwebtask.entity.UserRecord;
import com.example.springwebtask.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService{
    @Autowired
    private IUserRepository userRepository;

    @Override
    public UserRecord findUser(String loginId, String pass) {
        return userRepository.findUser(loginId, pass);
    }
}
