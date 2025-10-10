package com.eshop.MenuService.Serive;


import com.eshop.MenuService.DTO.CreateOrUpdateMenuItemDto;
import com.eshop.MenuService.DTO.MenuItemDto;
import com.eshop.MenuService.Model.MenuCategory;

import java.util.List;

public interface IMenuService {
    // READ
    List<MenuItemDto> getMenuItemsByCategoryId(Integer Id);
    MenuItemDto getMenuItemById(Integer id);
    List<MenuCategory> getAllCategories();

    // CREATE
    void createMenuItem(CreateOrUpdateMenuItemDto request);

    // UPDATE
    boolean updateMenuItem(MenuItemDto request);

    // DELETE
    void deleteMenuItem(int id);
}
