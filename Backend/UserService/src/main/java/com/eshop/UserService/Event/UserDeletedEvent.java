package com.eshop.UserService.Event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Event khi user bị xoá từ Keycloak
 */
@Getter
public class UserDeletedEvent extends ApplicationEvent {

    private final String email;

    public UserDeletedEvent(Object source, String email) {
        super(source);
        this.email = email;
    }
}
