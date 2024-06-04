package com.example.springwebtask.entity;

import java.util.Date;

public record ProductRecord(int id, String productId, int categoryId,
                            String name, int price, String imagePath,
                            String description, Date createdAt,
                            Date updatedAt) {
}
