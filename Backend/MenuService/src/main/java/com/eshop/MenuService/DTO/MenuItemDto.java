package com.eshop.MenuService.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class MenuItemDto {
    private Integer id;

    @NotEmpty(message = "Tên món ăn không được để trống")
    private String name;


    private String description;

    @NotNull(message = "Giá không được để trống")
    @Positive(message = "Giá phải là một số dương")
    private Double price;

    @NotEmpty(message = "Slug không được để trống")
    @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug chỉ được chứa ký tự thường, số và dấu gạch ngang")
    private String slug;

    private String image;

    @NotNull(message = "Số lượng tồn kho không được để trống")
    @Min(value = 0, message = "Số lượng tồn kho không được là số âm")
    private Integer availableStock;

    @NotNull(message = "ID danh mục không được để trống")
    private Integer menuCategory;
}

