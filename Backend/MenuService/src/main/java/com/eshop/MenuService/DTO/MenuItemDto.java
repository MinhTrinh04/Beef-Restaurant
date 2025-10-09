package com.eshop.MenuService.DTO;

import lombok.Data;

@Data
public class MenuItemDto {

    private int id;
    private String name;
    private String description;
    private double price;
    private String slug;
    private String image;
    private int availableStock;
    private String categoryName; // Chỉ trả về tên category, không cần cả object
}

