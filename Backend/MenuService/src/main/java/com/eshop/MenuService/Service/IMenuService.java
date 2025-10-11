package com.eshop.MenuService.Service;


import com.eshop.MenuService.DTO.MenuItemDto;

import java.util.List;

public interface IMenuService {
    // READ
    List<MenuItemDto> getAllMenuItems();
    List<MenuItemDto> getMenuItemsByCategoryId(Integer Id);
    MenuItemDto getMenuItemById(Integer id);
    MenuItemDto getMenuItemBySlug(String slug);

    // CREATE
    void createMenuItem(MenuItemDto request);

    // UPDATE
    boolean updateMenuItem(MenuItemDto request);

    // DELETE
    void deleteMenuItem(Integer id);
}
