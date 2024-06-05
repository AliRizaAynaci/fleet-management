package com.example.fleetmanagement.service.interfaces;

import com.example.fleetmanagement.model.entity.Package;
import com.example.fleetmanagement.model.enums.ShipmentState;

public interface BranchService {
    ShipmentState unloadPackage(Package packageItem);
}
