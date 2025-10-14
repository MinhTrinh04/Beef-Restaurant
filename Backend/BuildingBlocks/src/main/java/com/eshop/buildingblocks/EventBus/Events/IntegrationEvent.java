package com.eshop.buildingblocks.EventBus.Events;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public abstract class IntegrationEvent {
    private UUID id;
    private LocalDateTime creationDate;

    public IntegrationEvent() {
        this.id = UUID.randomUUID();
        this.creationDate = LocalDateTime.now();
    }
//    @JsonCreator
//    public IntegrationEvent(@JsonProperty("id") UUID id, @JsonProperty("creationDate") LocalDateTime creationDate) {
//        this.id = id;
//        this.creationDate = creationDate;
//    }
}
