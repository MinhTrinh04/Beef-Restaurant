package com.eshop.UserService.Event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * Event khi user được cập nhật từ Keycloak
 */
@Getter
public class UserUpdatedEvent extends ApplicationEvent {

    private final String email;
    private final String keycloakUserId;
    private final String firstName;
    private final String lastName;

    public UserUpdatedEvent(Object source, String email, String keycloakUserId, String firstName, String lastName) {
        super(source);
        this.email = email;
        this.keycloakUserId = keycloakUserId;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
