package com.example.fleetmanagement.service.interfaces;

import com.example.fleetmanagement.dto.DistributionRequestDTO;
import com.example.fleetmanagement.dto.DistributionResponseDto;
import com.example.fleetmanagement.dto.VehicleDto;

public interface VehicleService {

    DistributionResponseDto distribute(DistributionRequestDTO request, VehicleDto vehicle);

}
