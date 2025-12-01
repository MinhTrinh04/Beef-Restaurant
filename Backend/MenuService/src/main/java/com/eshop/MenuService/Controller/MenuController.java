package com.eshop.MenuService.Controller;

import com.eshop.MenuService.Constants.MenuConstants;
import com.eshop.MenuService.DTO.MenuItemDto;
import com.eshop.MenuService.DTO.ResponseDto;
import com.eshop.MenuService.DTO.StockValidationItem;
import com.eshop.MenuService.Service.IMenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/menu")
@RequiredArgsConstructor
@Slf4j
public class MenuController {
    private final IMenuService menuService;

    @PostMapping("/items")
    public ResponseEntity<ResponseDto> createMenuItem(
           @Valid @RequestBody MenuItemDto requestDto) {
        menuService.createMenuItem(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto(MenuConstants.STATUS_201, MenuConstants.MESSAGE_201));
    }

    @GetMapping("/items")
    public ResponseEntity<List<MenuItemDto>> getAllMenuItems() {
        List<MenuItemDto> items = menuService.getAllMenuItems();
        return ResponseEntity.status(HttpStatus.OK).body(items);
    }

    @GetMapping("/items/category/{id}")
    public ResponseEntity<List<MenuItemDto>> getMenuItemsByCategoryId(@PathVariable("id") Integer categoryId) {
        List<MenuItemDto> items = menuService.getMenuItemsByCategoryId(categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(items);
    }

    @GetMapping("/items/{id}")
    public ResponseEntity<MenuItemDto> getMenuItemById(@PathVariable Integer id) {
        MenuItemDto menuItem = menuService.getMenuItemById(id);
        return ResponseEntity.status(HttpStatus.OK).body(menuItem);
    }

    //Endpoint dùng cho nextjs nếu tiện ko thì fetch bằng id
    @GetMapping("/items/slug/{slug}")
    public ResponseEntity<MenuItemDto> getMenuItemBySlug(@PathVariable String slug) {
        MenuItemDto menuItem = menuService.getMenuItemBySlug(slug);
        return ResponseEntity.status(HttpStatus.OK).body(menuItem);
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<ResponseDto> updateMenuItem(@PathVariable Integer id,@Valid @RequestBody MenuItemDto requestDto) {
        requestDto.setId(id);
        boolean isSuccess = menuService.updateMenuItem(requestDto);
        if (isSuccess) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(MenuConstants.STATUS_200, MenuConstants.MESSAGE_200));
        } else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseDto(MenuConstants.STATUS_417, MenuConstants.MESSAGE_417_UPDATE));
        }
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<ResponseDto> deleteMenuItem(@PathVariable Integer id) {
        boolean isDeleted = menuService.deleteMenuItem(id);
        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(MenuConstants.STATUS_200, MenuConstants.MESSAGE_200));
        } else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseDto(MenuConstants.STATUS_417, MenuConstants.MESSAGE_417_DELETE));
        }
    }

    @PostMapping("/validate-stock")
    public ResponseEntity<Void> validateStock(@RequestBody List<StockValidationItem> items) {
        menuService.validateStockAvailability(items);
        return ResponseEntity.ok().build();
    }

}
