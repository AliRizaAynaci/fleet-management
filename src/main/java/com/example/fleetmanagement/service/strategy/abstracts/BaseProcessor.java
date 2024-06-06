package com.example.fleetmanagement.service.strategy.abstracts;

import com.example.fleetmanagement.model.entity.Package;
import com.example.fleetmanagement.model.entity.Sack;
import com.example.fleetmanagement.model.enums.DeliveryPoint;
import com.example.fleetmanagement.model.enums.ShipmentState;
import com.example.fleetmanagement.repository.PackageRepository;
import com.example.fleetmanagement.repository.SackRepository;
import com.example.fleetmanagement.service.interfaces.BranchService;
import com.example.fleetmanagement.service.interfaces.DistributionCenterService;
import com.example.fleetmanagement.service.interfaces.TransferCenterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseProcessor {

    protected static final Logger logger = LoggerFactory.getLogger(BaseProcessor.class);

    protected final PackageRepository packageRepository;
    protected final SackRepository sackRepository;
    protected final DistributionCenterService distributionCenterService;
    protected final BranchService branchService;
    protected final TransferCenterService transferCenterService;

    protected BaseProcessor(PackageRepository packageRepository, SackRepository sackRepository, DistributionCenterService distributionCenterService, BranchService branchService, TransferCenterService transferCenterService) {
        this.packageRepository = packageRepository;
        this.sackRepository = sackRepository;
        this.distributionCenterService = distributionCenterService;
        this.branchService = branchService;
        this.transferCenterService = transferCenterService;
    }

    protected ShipmentState processSack(Sack sack, DeliveryPoint deliveryPoint) {
        logger.info("Processing sack with barcode: {} at delivery point: {}",
                sack.getBarcode(), deliveryPoint);

        if (deliveryPoint == sack.getDeliveryPoint()) {
            if (deliveryPoint == DeliveryPoint.DISTRIBUTION_CENTER) {
                return distributionCenterService.unloadSack(sack);
            } else if (deliveryPoint == DeliveryPoint.TRANSFER_CENTER) {
                return transferCenterService.unloadSack(sack);
            }
        }
        sack.setState(ShipmentState.LOADED);
        sackRepository.save(sack);
        return sack.getState();
    }

    protected ShipmentState processPackage(Package packageItem, DeliveryPoint deliveryPoint) {
        logger.info("Processing package with barcode: {} at delivery point: {}",
                packageItem.getBarcode(), deliveryPoint);

        if (deliveryPoint == packageItem.getDeliveryPoint()) {
            if (deliveryPoint == DeliveryPoint.BRANCH) {
                return branchService.unloadPackage(packageItem);
            } else if (deliveryPoint == DeliveryPoint.DISTRIBUTION_CENTER) {
                return distributionCenterService.unloadPackage(packageItem);
            } else if (deliveryPoint == DeliveryPoint.TRANSFER_CENTER) {
                return transferCenterService.unloadPackage(packageItem);
            }
        }
        packageItem.setState(ShipmentState.LOADED);
        packageRepository.save(packageItem);
        return packageItem.getState();
    }
}
