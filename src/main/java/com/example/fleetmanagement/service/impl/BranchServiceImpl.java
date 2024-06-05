package com.example.fleetmanagement.service.impl;

import com.example.fleetmanagement.model.Package;
import com.example.fleetmanagement.model.Sack;
import com.example.fleetmanagement.model.enums.DeliveryPoint;
import com.example.fleetmanagement.model.enums.ShipmentState;
import com.example.fleetmanagement.repository.PackageRepository;
import com.example.fleetmanagement.service.interfaces.BranchService;
import org.springframework.stereotype.Service;

@Service
public class BranchServiceImpl implements BranchService {

    private final PackageRepository packageRepository;

    public BranchServiceImpl(PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    @Override
    public ShipmentState unloadPackage(Package packageItem) {

        if (packageItem.getDeliveryPoint() == DeliveryPoint.BRANCH) {
            if (packageItem.getSack() != null) {
                throw new IllegalArgumentException("This package is in a sack. " +
                        "It must be unloaded at a Distribute Center or Transfer Center.");
            }
            packageItem.setState(ShipmentState.UNLOADED);
            packageItem.setSack(null);
            packageRepository.save(packageItem);
            return ShipmentState.UNLOADED;
        } else {
            throw new IllegalArgumentException("This package cannot be unloaded at this branch.");
        }
    }
}
