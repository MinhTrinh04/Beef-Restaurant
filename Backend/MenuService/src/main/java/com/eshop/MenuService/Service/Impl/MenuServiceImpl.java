package com.eshop.MenuService.Service.Impl;

import com.eshop.MenuService.DTO.MenuItemDto;
import com.eshop.MenuService.DTO.StockValidationItem;
import com.eshop.MenuService.Exception.MenuItemAlreadyExistsException;
import com.eshop.MenuService.Exception.ResourceNotFoundException;
import com.eshop.MenuService.Exception.StockValidationException;
import com.eshop.MenuService.Repository.MenuCategoryRepository;
import com.eshop.MenuService.Repository.MenuItemRepository;
import com.eshop.MenuService.Mapper.MenuItemsMapper;
import com.eshop.MenuService.Model.MenuItem;
import com.eshop.MenuService.Service.IMenuService;
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
        if (!menuItemRepository.existsById(id)) {
            log.error("MenuCategory not found with id: {}", id);
            throw new ResourceNotFoundException("MenuItemsByCategory", "id", id.toString());
        }
        List<MenuItem> menuItems = menuItemRepository.findByMenuCategory(id).orElseThrow(() -> {
            log.error("No MenuItems found for category id: {}", id);
            throw new ResourceNotFoundException("MenuItemsByCategory", "id", id.toString());
        });
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
        if (optionalMenuItem.isPresent()) {
            throw new MenuItemAlreadyExistsException("MenuItem exists with the given slug " + request.getSlug());
        }
        log.info("Create MenuItem successfully");
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
    public boolean deleteMenuItem(Integer id) {
        MenuItem menuItem = menuItemRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("MenuItem", "id", id.toString()));
        menuItemRepository.deleteById(id);
        return true;
    }

    @Override
    public void validateStockAvailability(List<StockValidationItem> items) {
        for (StockValidationItem item : items) {
            MenuItem menuItem = menuItemRepository.findById(item.getMenuItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("MenuItem", "id", item.getMenuItemId().toString()));

            if (menuItem.getAvailableStock() < item.getQuantity()) {
                throw new StockValidationException("Not enough stock for item: " + menuItem.getName());
            }
        }
    }
}
