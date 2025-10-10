package com.eshop.MenuService.Serive.Impl;

import com.eshop.MenuService.DTO.CreateOrUpdateMenuItemDto;
import com.eshop.MenuService.DTO.MenuItemDto;
import com.eshop.MenuService.Exception.ResourceNotFoundException;
import com.eshop.MenuService.Infrastructure.Repository.MenuCategoryRepository;
import com.eshop.MenuService.Infrastructure.Repository.MenuItemRepository;
import com.eshop.MenuService.Mapper.MenuItemsMapper;
import com.eshop.MenuService.Model.MenuCategory;
import com.eshop.MenuService.Model.MenuItem;
import com.eshop.MenuService.Serive.IMenuService;
import jakarta.persistence.EntityNotFoundException;
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
        MenuItem menuItem = menuItemRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("MenuItem", "id", id.toString()));
        MenuItemDto menuItemDto = MenuItemsMapper.mapToMenuItemDto(menuItem,new MenuItemDto());

        return menuItemDto;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MenuCategory> getAllCategories() {
        return menuCategoryRepository.findAll();
    }

    @Override
    @Transactional
    public void createMenuItem(CreateOrUpdateMenuItemDto request) {
        MenuCategory category = findMenuCategoryById(request.getCategoryId());

//        MenuItem newMenuItem = new MenuItem();
//        newMenuItem.setName(request.getName());
//        newMenuItem.setDescription(request.getDescription());
//        newMenuItem.setPrice(request.getPrice());
//        newMenuItem.setSlug(request.getSlug());
//        newMenuItem.setImage(request.getImage());
//        newMenuItem.setAvailableStock(request.getAvailableStock());
//        newMenuItem.setMenuCategory(category);
//
//        MenuItem savedItem = menuItemRepository.save(newMenuItem);
//        return convertToDto(savedItem);
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
    public void deleteMenuItem(int id) {
        if (!menuItemRepository.existsById(id)) {
            throw new EntityNotFoundException("Không tìm thấy món ăn với ID: " + id + " để xóa.");
        }
        menuItemRepository.deleteById(id);
    }

    // Helper

    private MenuItem findMenuItemById(int id) {
        return menuItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy món ăn với ID: " + id));
    }

    private MenuCategory findMenuCategoryById(int id) {
        return menuCategoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy danh mục với ID: " + id));
    }

//    private MenuItemDto convertToDto(MenuItem menuItem) {
//        MenuItemDto dto = new MenuItemDto();
//        dto.setId(menuItem.getId());
//        dto.setName(menuItem.getName());
//        dto.setDescription(menuItem.getDescription());
//        dto.setPrice(menuItem.getPrice());
//        dto.setSlug(menuItem.getSlug());
//        dto.setImage(menuItem.getImage());
//        dto.setAvailableStock(menuItem.getAvailableStock());
//        if (menuItem.getMenuCategory() != null) {
//            dto.setCategoryName(menuItem.getMenuCategory().getName());
//        }
//        return dto;
//    }
}
