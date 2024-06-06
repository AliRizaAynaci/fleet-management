package com.example.fleetmanagement.service.strategy.interfaces;

import com.example.fleetmanagement.dto.DeliveryItemDto;
import com.example.fleetmanagement.model.enums.DeliveryPoint;
import com.example.fleetmanagement.model.enums.ShipmentState;

public interface DeliveryItemProcessor {
    ShipmentState process(DeliveryItemDto deliveryItemDto, DeliveryPoint deliveryPoint);
}
