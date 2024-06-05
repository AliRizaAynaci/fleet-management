package com.example.fleetmanagement.service.interfaces;

import com.example.fleetmanagement.model.entity.Package;
import com.example.fleetmanagement.model.entity.Sack;
import com.example.fleetmanagement.model.enums.ShipmentState;

public interface DistributionCenterService {

    ShipmentState unloadSack(Sack sack);
    ShipmentState unloadPackage(Package packageItem);
}
