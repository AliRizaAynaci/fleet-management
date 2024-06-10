package com.example.fleetmanagement.service.impl;

import com.example.fleetmanagement.model.entity.Package;
import com.example.fleetmanagement.model.entity.Sack;
import com.example.fleetmanagement.model.enums.DeliveryPoint;
import com.example.fleetmanagement.model.enums.ShipmentState;
import com.example.fleetmanagement.repository.PackageRepository;
import com.example.fleetmanagement.repository.SackRepository;
import com.example.fleetmanagement.service.interfaces.DistributionCenterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DistributionCenterServiceImpl implements DistributionCenterService {

    private static final Logger logger = LoggerFactory.getLogger(DistributionCenterServiceImpl.class);

    private final PackageRepository packageRepository;
    private final SackRepository sackRepository;

    public DistributionCenterServiceImpl(PackageRepository packageRepository, SackRepository sackRepository) {
        this.packageRepository = packageRepository;
        this.sackRepository = sackRepository;
    }

    @Override
    @Transactional
    public ShipmentState unloadSack(Sack sack) {
        if (sack.getDeliveryPoint() != DeliveryPoint.DISTRIBUTION_CENTER) {
            logger.warn("Sack with barcode {} cannot be unloaded at this distribution center.",
                    sack.getBarcode());
            throw new IllegalArgumentException("This sack cannot be unloaded at this distribution center.");
        }
        if (sack.getState() == ShipmentState.UNLOADED) {
            logger.warn("Sack with barcode {} is already unloaded.",
                    sack.getBarcode());
            throw new IllegalArgumentException("This sack is already unloaded.");
        }

        sack = sackRepository.findById(sack.getId()).orElseThrow(() -> new RuntimeException("Sack not found"));

        List<Package> packages = sack.getPackages();
        for (Package packageItem : packages) {
            unloadPackage(packageItem);
        }

        sack.setState(ShipmentState.UNLOADED);
        sackRepository.save(sack);
        logger.info("Sack with barcode {} unloaded at distribution center.",
                sack.getBarcode());
        return sack.getState();
    }

    @Override
    public ShipmentState unloadPackage(Package packageItem) {
        if (packageItem.getDeliveryPoint() != DeliveryPoint.DISTRIBUTION_CENTER) {
            logger.warn("Package with barcode {} cannot be unloaded at this distribution center.",
                    packageItem.getBarcode());
            throw new IllegalArgumentException("This package cannot be unloaded at this distribution center.");
        }

        if (packageItem.getState() == ShipmentState.UNLOADED) {
            logger.warn("Package with barcode {} is already unloaded.",
                    packageItem.getBarcode());
            throw new IllegalArgumentException("This package is already unloaded.");
        }

        packageItem.setState(ShipmentState.UNLOADED);
        packageRepository.save(packageItem);
        logger.info("Package with barcode {} unloaded at distribution center.",
                packageItem.getBarcode());
        return packageItem.getState();
    }
}
