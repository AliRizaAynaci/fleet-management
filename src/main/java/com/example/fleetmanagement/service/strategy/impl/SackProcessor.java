package com.example.fleetmanagement.service.strategy.impl;

import com.example.fleetmanagement.dto.DeliveryItemDto;
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
public class SackProcessor extends BaseProcessor implements DeliveryItemProcessor {

    protected SackProcessor(PackageRepository packageRepository,
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
        Sack sack = sackRepository.findByBarcode(barcode)
                .orElseThrow(() -> new IllegalArgumentException("Sack not found with barcode: " + barcode));

        if (sack.getState() == null) {
            sack.setState(ShipmentState.CREATED);
        }

        return processSack(sack, deliveryPoint);
    }
}
