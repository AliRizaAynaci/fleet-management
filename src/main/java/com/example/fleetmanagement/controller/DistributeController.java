package com.example.fleetmanagement.controller;

import com.example.fleetmanagement.dto.DistributionRequestDTO;
import com.example.fleetmanagement.dto.DistributionResponseDto;
import com.example.fleetmanagement.dto.VehicleDto;
import com.example.fleetmanagement.service.interfaces.VehicleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/vehicles")
public class DistributeController {

    private final VehicleService vehicleService;

    public DistributeController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping("/{vehiclePlate}/distribute")
    public ResponseEntity<DistributionResponseDto> distribute(@PathVariable String vehiclePlate,
                                                              @RequestBody DistributionRequestDTO request) {
        DistributionResponseDto response = vehicleService.distribute(request, vehiclePlate);
        return ResponseEntity.ok(response);
    }
}
