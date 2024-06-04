package com.example.fleetmanagement.dto;

import com.example.fleetmanagement.model.enums.DeliveryPoint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteDto {
    private DeliveryPoint deliveryPoint;
    private List<DeliveryItemDto> deliveries;
}
