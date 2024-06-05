package com.example.fleetmanagement.service.impl;

import com.example.fleetmanagement.dto.*;
import com.example.fleetmanagement.model.entity.Package;
import com.example.fleetmanagement.model.entity.Sack;
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
    public DistributionResponseDto distribute(DistributionRequestDTO request, String vehicle) {
        DistributionResponseDto response = new DistributionResponseDto();
        response.setVehicle(vehicle);

        List<RouteDto> responseRoutes = new ArrayList<>();

        for (RouteDto route : request.getRoute()) {
            DeliveryPoint deliveryPoint = route.getDeliveryPoint();
            List<DeliveryItemDto> deliveryItems = new ArrayList<>();

            for (DeliveryItemDto deliveryItemDto : route.getDeliveries()) {
                String barcode = deliveryItemDto.getBarcode();
                ShipmentState newState;
                // if the barcode starts with P, we need to process the package
                if (barcode.startsWith("P")) {
                    Package packageItem = packageRepository.findByBarcode(barcode)
                            .orElseThrow(() -> new IllegalArgumentException("Package not found with barcode: " + barcode));

                    // if package is created, we need to process the package
                    if (packageItem.getState() == ShipmentState.CREATED) {
                        newState = processPackage(packageItem, deliveryPoint);
                        packageRepository.save(packageItem);
                    }
                    // if package is loaded into sack, we need to process the sack
                    else if (packageItem.getState() == ShipmentState.LOADED) {
                        newState = processSack(packageItem.getSack(), deliveryPoint);
                        packageRepository.save(packageItem);
                    }
                    else {
                        // if package is in any other state, we need to update the state
                        newState = packageItem.getState();
                    }
                }
                // if the barcode starts without P, we need to process the sack
                else {
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
        if (deliveryPoint == packageItem.getDeliveryPoint()) {
            if (deliveryPoint == DeliveryPoint.DISTRIBUTION_CENTER) {
                return distributionCenterService.unloadPackage(packageItem);
            } else if (deliveryPoint == DeliveryPoint.BRANCH) {
                return branchService.unloadPackage(packageItem);
            }
        }
        packageItem.setState(ShipmentState.LOADED);
        packageRepository.save(packageItem);
        return packageItem.getState();
    }

    private ShipmentState processSack(Sack sack, DeliveryPoint deliveryPoint) {
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
}
