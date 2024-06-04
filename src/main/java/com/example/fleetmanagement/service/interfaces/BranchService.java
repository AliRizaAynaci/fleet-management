package com.example.fleetmanagement.service.interfaces;

import com.example.fleetmanagement.model.Package;
import com.example.fleetmanagement.model.Sack;
import com.example.fleetmanagement.model.enums.ShipmentState;

public interface BranchService {
    ShipmentState unloadPackage(Package packageItem);
}
