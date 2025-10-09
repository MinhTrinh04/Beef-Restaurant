package com.eshop.MenuService.Serive;


import com.eshop.MenuService.DTO.CreateOrUpdateMenuItemDto;
import com.eshop.MenuService.DTO.MenuItemDto;
import com.eshop.MenuService.Model.MenuCategory;

import java.util.List;

public interface IMenuService {
    // READ
    List<MenuItemDto> getAllMenuItems();
    MenuItemDto getMenuItemById(int id);
    List<MenuCategory> getAllCategories();

    // CREATE
    MenuItemDto createMenuItem(CreateOrUpdateMenuItemDto request);

    // UPDATE
    MenuItemDto updateMenuItem(int id, CreateOrUpdateMenuItemDto request);

    // DELETE
    void deleteMenuItem(int id);
}
