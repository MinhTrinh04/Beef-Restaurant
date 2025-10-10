package com.eshop.MenuService.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class MenuItemAlreadyExistsException extends RuntimeException {
    public MenuItemAlreadyExistsException(String message) {
        super(message);
    }
}
