package com.example.fleetmanagement.service.impl;

import com.example.fleetmanagement.dto.*;
import com.example.fleetmanagement.model.Package;
import com.example.fleetmanagement.model.Sack;
import com.example.fleetmanagement.model.enums.DeliveryPoint;
import com.example.fleetmanagement.model.enums.ShipmentState;
import com.example.fleetmanagement.repository.PackageRepository;
import com.example.fleetmanagement.repository.SackRepository;
import com.example.fleetmanagement.service.interfaces.BranchService;
import com.example.fleetmanagement.service.interfaces.DistributionCenterService;
import com.example.fleetmanagement.service.interfaces.TransferCenterService;
import com.example.fleetmanagement.service.interfaces.VehicleService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VehicleServiceImpl implements VehicleService {

    private final PackageRepository packageRepository;
    private final SackRepository sackRepository;
    private final DistributionCenterService distributionCenterService;
    private final BranchService branchService;
    private final TransferCenterService transferCenterService;

    public VehicleServiceImpl(PackageRepository packageRepository, SackRepository sackRepository,
                              DistributionCenterService distributionCenterService,
                              BranchService branchService, TransferCenterService transferCenterService) {
        this.packageRepository = packageRepository;
        this.sackRepository = sackRepository;
        this.distributionCenterService = distributionCenterService;
        this.branchService = branchService;
        this.transferCenterService = transferCenterService;
    }

    @Override
    public DistributionResponseDto distribute(DistributionRequestDTO request) {
        DistributionResponseDto response = new DistributionResponseDto();
        response.setVehicle(request.getVehicle());

        List<RouteDto> responseRoutes = new ArrayList<>();

        for (RouteDto route : request.getRoute()) {
            DeliveryPoint deliveryPoint = route.getDeliveryPoint();
            List<DeliveryItemDto> deliveryItems = new ArrayList<>();

            for (DeliveryItemDto deliveryItemDto : route.getDeliveries()) {
                String barcode = deliveryItemDto.getBarcode();
                ShipmentState newState;
                if (barcode.startsWith("P")) {
                    Package packageItem = packageRepository.findByBarcode(barcode)
                            .orElseThrow(() -> new IllegalArgumentException("Package not found with barcode: " + barcode));
                    newState = processPackage(packageItem, deliveryPoint);
                    packageRepository.save(packageItem);
                } else {
                    Sack sack = sackRepository.findByBarcode(barcode)
                            .orElseThrow(() -> new IllegalArgumentException("Sack not found with barcode: " + barcode));
                    newState = processSack(sack, deliveryPoint);
                    sackRepository.save(sack);
                }
                deliveryItems.add(new DeliveryItemDto(barcode, newState));
            }
            responseRoutes.add(new RouteDto(deliveryPoint, deliveryItems));
        }
        response.setRoute(responseRoutes);
        return response;
    }

    private ShipmentState processPackage(Package packageItem, DeliveryPoint deliveryPoint) {
        if (packageItem.getState() == ShipmentState.LOADED) {
            if (deliveryPoint == packageItem.getDeliveryPoint()) {
                switch (deliveryPoint) {
                    case DISTRIBUTION_CENTER:
                        return distributionCenterService.unloadPackage(packageItem);
                    case BRANCH:
                        return branchService.unloadPackage(packageItem);
                    default:
                        throw new IllegalArgumentException("Package cannot be unloaded at this delivery point.");
                }
            } else {
                throw new IllegalArgumentException("Package cannot be loaded at this delivery point.");
            }
        } else {
            throw new IllegalArgumentException("Invalid package state.");
        }
    }

    private ShipmentState processSack(Sack sack, DeliveryPoint deliveryPoint) {
        if (sack.getState() == ShipmentState.LOADED) {
            if (deliveryPoint == sack.getDeliveryPoint()) {
                switch (deliveryPoint) {
                    case DISTRIBUTION_CENTER:
                        return distributionCenterService.unloadSack(sack);
                    case TRANSFER_CENTER:
                        return transferCenterService.unloadSack(sack);
                    default:
                        throw new IllegalArgumentException("Sack cannot be unloaded at this delivery point.");
                }
            } else {
                throw new IllegalArgumentException("Sack cannot be unloaded at this delivery point.");
            }
        } else {
            throw new IllegalArgumentException("Invalid sack state.");
        }
    }
}
