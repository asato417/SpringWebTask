package com.example.springwebtask.entity;

import java.util.Date;

public record UserRecord(int id, String loginId, String password,
                         String name, int role, Date createdAt,
                         Date updatedAt) {}
