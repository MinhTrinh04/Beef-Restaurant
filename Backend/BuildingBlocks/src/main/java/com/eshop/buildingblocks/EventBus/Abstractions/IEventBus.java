package com.eshop.buildingblocks.EventBus.Abstractions;

import com.eshop.buildingblocks.EventBus.Events.IntegrationEvent;

public interface IEventBus {
    void publish(IntegrationEvent event);
    

}
