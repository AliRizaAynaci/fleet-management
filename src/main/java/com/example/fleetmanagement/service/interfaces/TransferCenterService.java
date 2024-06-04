package com.example.fleetmanagement.service.interfaces;

import com.example.fleetmanagement.model.Sack;
import com.example.fleetmanagement.model.enums.ShipmentState;

public interface TransferCenterService {
    ShipmentState unloadSack(Sack sack);
}
