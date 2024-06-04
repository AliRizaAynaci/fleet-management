package com.example.fleetmanagement.service.impl;

import com.example.fleetmanagement.model.Package;
import com.example.fleetmanagement.model.Sack;
import com.example.fleetmanagement.model.enums.DeliveryPoint;
import com.example.fleetmanagement.model.enums.ShipmentState;
import com.example.fleetmanagement.repository.PackageRepository;
import com.example.fleetmanagement.repository.SackRepository;
import com.example.fleetmanagement.service.interfaces.DistributionCenterService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DistributionCenterServiceImpl implements DistributionCenterService {

    private final PackageRepository packageRepository;
    private final SackRepository sackRepository;

    public DistributionCenterServiceImpl(PackageRepository packageRepository, SackRepository sackRepository) {
        this.packageRepository = packageRepository;
        this.sackRepository = sackRepository;
    }

    @Override
    public ShipmentState unloadSack(Sack sack) {
        if (sack.getDeliveryPoint() != DeliveryPoint.DISTRIBUTION_CENTER) {
            throw new IllegalArgumentException("This sack cannot be unloaded at this distribution center.");
        }
        if (sack.getState() == ShipmentState.UNLOADED) {
            throw new IllegalArgumentException("This sack is already unloaded.");
        }

        sack = sackRepository.findById(sack.getId()).orElseThrow(() -> new RuntimeException("Sack not found"));

        List<Package> packages = sack.getPackages();
        for (Package packageItem : packages) {
            unloadPackage(packageItem);
        }

        sack.setState(ShipmentState.UNLOADED);
        sackRepository.save(sack);
        return ShipmentState.UNLOADED;
    }

    @Override
    public ShipmentState unloadPackage(Package packageItem) {
        if (packageItem.getDeliveryPoint() != DeliveryPoint.DISTRIBUTION_CENTER) {
            throw new IllegalArgumentException("This package cannot be unloaded at this distribution center.");
        }

        if (packageItem.getState() == ShipmentState.UNLOADED) {
            throw new IllegalArgumentException("This package is already unloaded.");
        }

        packageItem.setSack(null);
        packageItem.setState(ShipmentState.UNLOADED);
        packageRepository.save(packageItem);
        return ShipmentState.UNLOADED;
    }
}
