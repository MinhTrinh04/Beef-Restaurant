package com.eshop.MenuService.DTO;

import com.eshop.MenuService.Model.MenuCategory;
import lombok.Data;

@Data
public class MenuItemDto {
    private Integer id;
    private String name;
    private String description;
    private Double price;
    private String slug;
    private String image;
    private Integer availableStock;
    private MenuCategory menuCategoryId; // Chỉ trả về tên category, không cần cả object
}

