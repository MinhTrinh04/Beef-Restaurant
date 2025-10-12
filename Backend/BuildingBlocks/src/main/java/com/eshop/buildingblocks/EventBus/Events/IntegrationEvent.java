package com.eshop.buildingblocks.EventBus.Events;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public abstract class IntegrationEvent {
    private UUID id;
    private Instant creationDate;

    public IntegrationEvent() {
        this.id = UUID.randomUUID();
        this.creationDate = Instant.now();
    }
}
