package com.example.fleetmanagement.service.interfaces;

import com.example.fleetmanagement.dto.DistributionRequestDTO;
import com.example.fleetmanagement.dto.DistributionResponseDto;

public interface VehicleService {

    DistributionResponseDto distribute(DistributionRequestDTO request);

}
