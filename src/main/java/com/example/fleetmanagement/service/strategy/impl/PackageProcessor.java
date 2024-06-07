package com.example.fleetmanagement.service.strategy.impl;

import com.example.fleetmanagement.dto.DeliveryItemDto;
import com.example.fleetmanagement.model.entity.Package;
import com.example.fleetmanagement.model.entity.Sack;
import com.example.fleetmanagement.model.enums.DeliveryPoint;
import com.example.fleetmanagement.model.enums.ShipmentState;
import com.example.fleetmanagement.repository.PackageRepository;
import com.example.fleetmanagement.repository.SackRepository;
import com.example.fleetmanagement.service.interfaces.BranchService;
import com.example.fleetmanagement.service.interfaces.DistributionCenterService;
import com.example.fleetmanagement.service.interfaces.TransferCenterService;
import com.example.fleetmanagement.service.strategy.abstracts.BaseProcessor;
import com.example.fleetmanagement.service.strategy.interfaces.DeliveryItemProcessor;
import org.springframework.stereotype.Service;

@Service
public class PackageProcessor extends BaseProcessor implements DeliveryItemProcessor {

    public PackageProcessor(PackageRepository packageRepository,
                            SackRepository sackRepository,
                            DistributionCenterService distributionCenterService,
                            BranchService branchService,
                            TransferCenterService transferCenterService) {
        super(packageRepository, sackRepository, distributionCenterService,
                branchService, transferCenterService);
    }

    @Override
    public ShipmentState process(DeliveryItemDto deliveryItemDto, DeliveryPoint deliveryPoint) {
        String barcode = deliveryItemDto.getBarcode();
        Package packageItem = packageRepository.findByBarcode(barcode)
                .orElseThrow(() -> new IllegalArgumentException("Package not found with barcode: " + barcode));

        if (packageItem.getState() == null) {
            packageItem.setState(ShipmentState.CREATED);
        }

        return processPackage(packageItem, deliveryPoint);
    }
}
