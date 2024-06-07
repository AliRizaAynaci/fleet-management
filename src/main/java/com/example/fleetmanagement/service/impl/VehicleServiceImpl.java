package com.example.fleetmanagement.service.impl;

import com.example.fleetmanagement.dto.*;
import com.example.fleetmanagement.model.enums.DeliveryPoint;
import com.example.fleetmanagement.model.enums.ShipmentState;
import com.example.fleetmanagement.service.interfaces.VehicleService;
import com.example.fleetmanagement.service.strategy.impl.PackageProcessor;
import com.example.fleetmanagement.service.strategy.impl.SackProcessor;
import com.example.fleetmanagement.service.strategy.interfaces.DeliveryItemProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VehicleServiceImpl implements VehicleService {

    private static final Logger logger = LoggerFactory.getLogger(VehicleServiceImpl.class);

    private final Map<String, DeliveryItemProcessor> processorMap = new HashMap<>();

    public VehicleServiceImpl(PackageProcessor packageProcessor, SackProcessor sackProcessor) {
        processorMap.put("P", packageProcessor);
        processorMap.put("C", sackProcessor);
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
                String prefix = barcode.substring(0, 1);
                DeliveryItemProcessor processor = processorMap.get(prefix);

                if (processor == null) {
                    throw new IllegalArgumentException("Unsupported barcode prefix: " + prefix);
                }

                logger.info("Processing delivery item with barcode: {}", barcode);
                ShipmentState newState = processor.process(deliveryItemDto, deliveryPoint);
                logger.info("Processed item with barcode: {}, new state: {}", barcode, newState);

                deliveryItems.add(new DeliveryItemDto(barcode, newState));
            }
            responseRoutes.add(new RouteDto(deliveryPoint, deliveryItems));
        }
        response.setRoute(responseRoutes);
        return response;
    }
}