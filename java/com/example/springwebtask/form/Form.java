package com.example.springwebtask.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Data
public class Form {
    @NotBlank(message = "商品IDは必須です")
    String productId;
    @NotBlank(message = "商品名は必須です")
    String name;
    @NotNull(message = "単価は必須です")
    Integer price;
    @NotBlank(message = "カテゴリは必須です")
    String category;
    String description;
    MultipartFile file;
}
