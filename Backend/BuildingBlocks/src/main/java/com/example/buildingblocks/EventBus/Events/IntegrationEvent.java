package com.example.buildingblocks.EventBus.Events;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
@AllArgsConstructor
public abstract class IntegrationEvent {
    public UUID id;
    public Instant creationDate;
}
