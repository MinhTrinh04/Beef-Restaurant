package com.eshop.MenuService.Controller;

import com.eshop.MenuService.DTO.StockValidationItem;
import com.eshop.MenuService.Service.IMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v2/menu")
@RequiredArgsConstructor
@Slf4j
public class MenuControllerV2 {
    private final IMenuService menuService;

    @PostMapping("/validate-stock")
    public ResponseEntity<Void> validateStock(@RequestBody List<StockValidationItem> items) {
        menuService.validateStockAvailability(items);
        return ResponseEntity.ok().build();
    }

}
