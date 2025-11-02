package com.eshop.buildingblocks.EventBus.Abstractions;
import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;


public interface IIntegrationEventHandler<T extends  IntegrationEvent> {

    void handle(T event);
}
