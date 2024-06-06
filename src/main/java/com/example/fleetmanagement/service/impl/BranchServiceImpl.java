package com.example.fleetmanagement.service.impl;

import com.example.fleetmanagement.model.entity.Package;
import com.example.fleetmanagement.model.enums.DeliveryPoint;
import com.example.fleetmanagement.model.enums.ShipmentState;
import com.example.fleetmanagement.repository.PackageRepository;
import com.example.fleetmanagement.service.interfaces.BranchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BranchServiceImpl implements BranchService {

    private static final Logger logger = LoggerFactory.getLogger(BranchServiceImpl.class);

    private final PackageRepository packageRepository;

    public BranchServiceImpl(PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    @Override
    public ShipmentState unloadPackage(Package packageItem) {

        if (packageItem.getDeliveryPoint() == DeliveryPoint.BRANCH) {
            if (packageItem.getSack() != null) {
                logger.warn("Package with barcode {} is in a sack and cannot be unloaded at a branch.",
                        packageItem.getBarcode());
                throw new IllegalArgumentException("This package is in a sack. " +
                        "It must be unloaded at a Distribute Center or Transfer Center.");
            }
            packageItem.setState(ShipmentState.UNLOADED);
            packageItem.setSack(null);
            packageRepository.save(packageItem);
            logger.info("Package with barcode {} unloaded at branch.",
                    packageItem.getBarcode());
            return ShipmentState.UNLOADED;
        } else {
            logger.warn("Package with barcode {} cannot be unloaded at this branch.",
                    packageItem.getBarcode());
            throw new IllegalArgumentException("This package cannot be unloaded at this branch.");
        }
    }
}
