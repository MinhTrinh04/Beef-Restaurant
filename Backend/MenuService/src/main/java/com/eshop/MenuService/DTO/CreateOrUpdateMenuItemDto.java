package com.eshop.MenuService.DTO;

import lombok.Data;

@Data
public class CreateOrUpdateMenuItemDto {
    private String name;
    private String description;
    private double price;
    private String slug;
    private String image;
    private int availableStock;
    private int categoryId; // Client chỉ cần gửi ID của category
}
