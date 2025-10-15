package com.eshop.buildingblocks.EventBus.Events;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public abstract class IntegrationEvent {
    private final UUID id;
    private final LocalDateTime creationDate;

    public IntegrationEvent() {
        this.id = UUID.randomUUID();
        this.creationDate = LocalDateTime.now();
    }
}
