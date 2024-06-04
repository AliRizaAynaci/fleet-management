package com.example.fleetmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistributionResponseDto {
    private VehicleDto vehicle;
    private List<RouteDto> route;
}
