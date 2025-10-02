package com.example.buildingblocks.EventBus.Abstractions;

import com.example.buildingblocks.EventBus.Events.IntegrationEvent;

public interface IEventBus {
    void publish(IntegrationEvent event);
}
