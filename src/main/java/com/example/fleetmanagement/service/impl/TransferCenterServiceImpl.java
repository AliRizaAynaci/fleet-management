package com.example.fleetmanagement.service.impl;

import com.example.fleetmanagement.model.entity.Package;
import com.example.fleetmanagement.model.entity.Sack;
import com.example.fleetmanagement.model.enums.DeliveryPoint;
import com.example.fleetmanagement.model.enums.ShipmentState;
import com.example.fleetmanagement.repository.PackageRepository;
import com.example.fleetmanagement.repository.SackRepository;
import com.example.fleetmanagement.service.interfaces.TransferCenterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransferCenterServiceImpl implements TransferCenterService {

    private static final Logger logger = LoggerFactory.getLogger(TransferCenterServiceImpl.class);

    private final PackageRepository packageRepository;
    private final SackRepository sackRepository;

    public TransferCenterServiceImpl(PackageRepository packageRepository, SackRepository sackRepository) {
        this.packageRepository = packageRepository;
        this.sackRepository = sackRepository;
    }

    @Override
    public ShipmentState unloadSack(Sack sack) {
        if (sack.getDeliveryPoint() != DeliveryPoint.TRANSFER_CENTER) {
            logger.warn("Sack with barcode {} cannot be unloaded at this transfer center.",
                    sack.getBarcode());
            throw new IllegalArgumentException("This sack cannot be unloaded at this transfer center.");
        }
        if (sack.getState() == ShipmentState.UNLOADED) {
            logger.warn("Sack with barcode {} is already unloaded.",
                    sack.getBarcode());
            throw new IllegalArgumentException("This sack is already unloaded.");
        }

        sack = sackRepository.findById(sack.getId()).orElseThrow(() -> new RuntimeException("Sack not found"));

        List<Package> packages = sack.getPackages();
        for (Package packageItem : packages) {
            if (packageItem.getDeliveryPoint() != DeliveryPoint.TRANSFER_CENTER) {
                logger.warn("Package with barcode {} cannot be unloaded at this transfer center.",
                        packageItem.getBarcode());
                throw new IllegalArgumentException("Package with barcode " + packageItem.getBarcode()
                        + " cannot be unloaded at this transfer center.");
            }
            packageItem.setState(ShipmentState.UNLOADED);
            packageItem.setSack(null);
            packageRepository.save(packageItem);
            logger.info("Package with barcode {} unloaded at transfer center.",
                    packageItem.getBarcode());
        }

        sack.setState(ShipmentState.UNLOADED);
        sackRepository.save(sack);
        logger.info("Sack with barcode {} unloaded at transfer center.",
                sack.getBarcode());
        return ShipmentState.UNLOADED;
    }
}
