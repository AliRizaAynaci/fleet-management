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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VehicleServiceImpl implements VehicleService {

    private static final Logger logger = LoggerFactory.getLogger(VehicleServiceImpl.class);

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

        logger.info("Starting distribution with vehicle: {}", vehicle);

        DistributionResponseDto response = new DistributionResponseDto();
        response.setVehicle(vehicle);

        List<RouteDto> responseRoutes = new ArrayList<>();

        for (RouteDto route : request.getRoute()) {
            DeliveryPoint deliveryPoint = route.getDeliveryPoint();
            List<DeliveryItemDto> deliveryItems = new ArrayList<>();

            for (DeliveryItemDto deliveryItemDto : route.getDeliveries()) {
                String barcode = deliveryItemDto.getBarcode();
                ShipmentState newState;

                logger.info("Processing delivery item with barcode: {}", barcode);

                // if the barcode starts with P, we need to process the package
                if (barcode.startsWith("P")) {
                    Package packageItem = packageRepository.findByBarcode(barcode)
                            .orElseThrow(() -> new IllegalArgumentException("Package not found with barcode: " + barcode));

                   if (packageItem.getState() == null) {
                       packageItem.setState(ShipmentState.CREATED);
                       newState = processPackageState(packageItem, deliveryPoint);
                       logger.info("Processed package with barcode: {}, new state: {}", barcode, newState);
                       packageRepository.save(packageItem);
                   } else {
                        // if package is in any other state, we need to update the state
                        newState = packageItem.getState();
                        logger.info("Package with barcode: {} is already in state: {}", barcode, newState);
                    }
                }
                // if the barcode starts without P, we need to process the sack
                else {
                    Sack sack = sackRepository.findByBarcode(barcode)
                            .orElseThrow(() -> new IllegalArgumentException("Sack not found with barcode: " + barcode));

                    // if sack state is null, set it to CREATED
                    if (sack.getState() == null) {
                        sack.setState(ShipmentState.CREATED);
                    }

                    newState = processSack(sack, deliveryPoint);
                    logger.info("Processed sack with barcode: {}, new state: {}", barcode, newState);
                    sackRepository.save(sack);
                }
                deliveryItems.add(new DeliveryItemDto(barcode, newState));
            }
            responseRoutes.add(new RouteDto(deliveryPoint, deliveryItems));
        }
        response.setRoute(responseRoutes);
        return response;
    }

    private ShipmentState processPackageState(Package packageItem, DeliveryPoint deliveryPoint) {

        if (packageItem.getSack() != null) {
            packageItem.setState(ShipmentState.LOADED_INTO_SACK);
            logger.info("Package with barcode: {} is loaded into sack.", packageItem.getBarcode());
            return processSack(packageItem.getSack(), deliveryPoint);
        } else {
            packageItem.setState(ShipmentState.LOADED);
            logger.info("Package with barcode: {} is loaded.", packageItem.getBarcode());
            return processPackage(packageItem, deliveryPoint);
        }

    }

    private ShipmentState processPackage(Package packageItem, DeliveryPoint deliveryPoint) {
        logger.info("Processing package with barcode: {} at delivery point: {}",
                packageItem.getBarcode(), deliveryPoint);

        if (deliveryPoint == packageItem.getDeliveryPoint()) {
            if (deliveryPoint == DeliveryPoint.DISTRIBUTION_CENTER) {
                return distributionCenterService.unloadPackage(packageItem);
            } else if (deliveryPoint == DeliveryPoint.BRANCH) {
                return branchService.unloadPackage(packageItem);
            }
        }
        packageRepository.save(packageItem);
        return packageItem.getState();
    }

    private ShipmentState processSack(Sack sack, DeliveryPoint deliveryPoint) {
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
}