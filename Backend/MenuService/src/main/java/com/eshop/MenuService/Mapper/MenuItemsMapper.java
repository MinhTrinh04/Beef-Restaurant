package com.eshop.MenuService.Mapper;

import com.eshop.MenuService.DTO.MenuItemDto;
import com.eshop.MenuService.Model.MenuItem;

public class MenuItemsMapper {

    public static MenuItemDto mapToMenuItemDto(MenuItem menuItem,MenuItemDto menuItemDto){
        menuItemDto.setId(menuItem.getId());
        menuItemDto.setName(menuItem.getName());
        menuItemDto.setDescription(menuItem.getDescription());
        menuItemDto.setPrice(menuItem.getPrice());
        menuItemDto.setSlug(menuItem.getSlug());
        menuItemDto.setImage(menuItem.getImage());
        menuItemDto.setAvailableStock(menuItem.getAvailableStock());
        menuItemDto.setMenuCategoryId(menuItem.getMenuCategoryId());

        return menuItemDto;
    }

    public static MenuItem mapToMenuItem(MenuItemDto menuItemDto,MenuItem menuItem){
//        menuItem.setId(menuItemDto.getId());
        menuItem.setName(menuItemDto.getName());
        menuItem.setDescription(menuItemDto.getDescription());
        menuItem.setPrice(menuItemDto.getPrice());
        menuItem.setSlug(menuItemDto.getSlug());
        menuItem.setImage(menuItemDto.getImage());
        menuItem.setAvailableStock(menuItemDto.getAvailableStock());
        menuItem.setMenuCategoryId(menuItemDto.getMenuCategoryId());

        return menuItem;
    }
}
