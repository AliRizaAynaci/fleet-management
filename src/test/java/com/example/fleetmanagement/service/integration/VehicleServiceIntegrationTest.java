package com.example.fleetmanagement.service.integration;

import com.example.fleetmanagement.dto.DeliveryItemDto;
import com.example.fleetmanagement.dto.DistributionRequestDTO;
import com.example.fleetmanagement.dto.DistributionResponseDto;
import com.example.fleetmanagement.dto.RouteDto;
import com.example.fleetmanagement.model.enums.DeliveryPoint;
import com.example.fleetmanagement.model.enums.ShipmentState;
import com.example.fleetmanagement.service.impl.VehicleServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class VehicleServiceIntegrationTest {

    @Autowired
    private VehicleServiceImpl vehicleService;

    @Test
    @Transactional
    void testDistribute() {

        DistributionRequestDTO request = new DistributionRequestDTO();

        RouteDto branchRoute = new RouteDto(DeliveryPoint.BRANCH, List.of(
                new DeliveryItemDto("P7988000121", null),
                new DeliveryItemDto("P7988000122", null),
                new DeliveryItemDto("P7988000123", null),
                new DeliveryItemDto("P8988000121", null),
                new DeliveryItemDto("C725799", null)
        ));

        RouteDto distributionCenterRoute = new RouteDto(DeliveryPoint.DISTRIBUTION_CENTER, List.of(
                new DeliveryItemDto("P8988000123", null),
                new DeliveryItemDto("P8988000124", null),
                new DeliveryItemDto("P8988000125", null),
                new DeliveryItemDto("C725799", null)
        ));

        RouteDto transferCenterRoute = new RouteDto(DeliveryPoint.TRANSFER_CENTER, List.of(
                new DeliveryItemDto("P9988000126", null),
                new DeliveryItemDto("P9988000127", null),
                new DeliveryItemDto("P9988000128", null),
                new DeliveryItemDto("P9988000129", null),
                new DeliveryItemDto("P9988000130", null)
        ));

        request.setRoute(List.of(branchRoute, distributionCenterRoute, transferCenterRoute));

        DistributionResponseDto response = vehicleService.distribute(request, "34TL34");

        // expected response
        DistributionResponseDto expectedResponse = new DistributionResponseDto();
        expectedResponse.setVehicle("34TL34");

        expectedResponse.setRoute(List.of(
                new RouteDto(DeliveryPoint.BRANCH, List.of(
                        new DeliveryItemDto("P7988000121", ShipmentState.UNLOADED),
                        new DeliveryItemDto("P7988000122", ShipmentState.UNLOADED),
                        new DeliveryItemDto("P7988000123", ShipmentState.UNLOADED),
                        new DeliveryItemDto("P8988000121", ShipmentState.LOADED),
                        new DeliveryItemDto("C725799", ShipmentState.LOADED)
                )),
                new RouteDto(DeliveryPoint.DISTRIBUTION_CENTER, List.of(
                        new DeliveryItemDto("P8988000123", ShipmentState.UNLOADED),
                        new DeliveryItemDto("P8988000124", ShipmentState.UNLOADED),
                        new DeliveryItemDto("P8988000125", ShipmentState.UNLOADED),
                        new DeliveryItemDto("C725799", ShipmentState.UNLOADED)
                )),
                new RouteDto(DeliveryPoint.TRANSFER_CENTER, List.of(
                        new DeliveryItemDto("P9988000126", ShipmentState.LOADED),
                        new DeliveryItemDto("P9988000127", ShipmentState.LOADED),
                        new DeliveryItemDto("P9988000128", ShipmentState.UNLOADED),
                        new DeliveryItemDto("P9988000129", ShipmentState.UNLOADED),
                        new DeliveryItemDto("P9988000130", ShipmentState.LOADED)
                ))
        ));

        assertEquals(expectedResponse, response);

    }

}
