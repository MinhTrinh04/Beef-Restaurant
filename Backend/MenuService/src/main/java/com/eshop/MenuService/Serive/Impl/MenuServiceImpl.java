package com.eshop.MenuService.Serive.Impl;

import com.eshop.MenuService.DTO.MenuItemDto;
import com.eshop.MenuService.Exception.MenuItemAlreadyExistsException;
import com.eshop.MenuService.Exception.ResourceNotFoundException;
import com.eshop.MenuService.Infrastructure.Repository.MenuCategoryRepository;
import com.eshop.MenuService.Infrastructure.Repository.MenuItemRepository;
import com.eshop.MenuService.Mapper.MenuItemsMapper;
import com.eshop.MenuService.Model.MenuItem;
import com.eshop.MenuService.Serive.IMenuService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class MenuServiceImpl implements IMenuService {
    private final MenuItemRepository menuItemRepository;
    private final MenuCategoryRepository menuCategoryRepository;

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemDto> getAllMenuItems() {
        List<MenuItem> menuItems = menuItemRepository.findAll();
        List<MenuItemDto> menuItemsDtos = menuItems.stream().map(menuItem -> MenuItemsMapper.mapToMenuItemDto(menuItem, new MenuItemDto())).collect(Collectors.toList());
        return menuItemsDtos;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuItemDto> getMenuItemsByCategoryId(Integer id) {
        Integer categoryId = menuCategoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("MenuCategory", "id", id.toString())).getId();
        Optional<MenuItem> menuItems = menuItemRepository.findByMenuCategoryId(categoryId);
        if (menuItems.isEmpty()) {
            throw new ResourceNotFoundException("MenuItemsByCategory", "id", id.toString());
        }
        List<MenuItemDto> menuItemsDtos = menuItems.stream().map(menuItem -> MenuItemsMapper.mapToMenuItemDto(menuItem, new MenuItemDto())).collect(Collectors.toList());

        return menuItemsDtos;
    }

    @Override
    @Transactional(readOnly = true)
    public MenuItemDto getMenuItemById(Integer id) {
        MenuItem menuItem = menuItemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("MenuItem", "id", id.toString()));
        MenuItemDto menuItemDto = MenuItemsMapper.mapToMenuItemDto(menuItem, new MenuItemDto());

        return menuItemDto;
    }

    @Override
    @Transactional(readOnly = true)
    public MenuItemDto getMenuItemBySlug(String slug) {
        MenuItem menuItem = menuItemRepository.findBySlug(slug).orElseThrow(() -> new ResourceNotFoundException("MenuItem", "slug", slug));
        MenuItemDto menuItemDto = MenuItemsMapper.mapToMenuItemDto(menuItem, new MenuItemDto());
        return menuItemDto;
    }

    @Override
    @Transactional
    public void createMenuItem(MenuItemDto request) {
        MenuItem menuItem = MenuItemsMapper.mapToMenuItem(request, new MenuItem());
        Optional<MenuItem> optionalMenuItem = menuItemRepository.findBySlug(request.getSlug());
        if (optionalMenuItem.isEmpty()) {
            throw new MenuItemAlreadyExistsException("MenuItem exists with the given slug" + request.getSlug());
        }
        menuItemRepository.save(menuItem);
    }

    @Override
    @Transactional
    public boolean updateMenuItem(MenuItemDto request) {
        boolean isUpdated = false;
        MenuItem existingItem = menuItemRepository.findById(request.getId()).orElseThrow(() -> new ResourceNotFoundException("MenuItem", "id", request.getId().toString()));
        MenuItemsMapper.mapToMenuItem(request, existingItem);
        menuItemRepository.save(existingItem);
        isUpdated = true;
        return isUpdated;
    }

    @Override
    @Transactional
    public void deleteMenuItem(Integer id) {
        MenuItem menuItem = menuItemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("MenuItem", "id", id.toString()));
        menuItemRepository.deleteById(id);
    }
}
