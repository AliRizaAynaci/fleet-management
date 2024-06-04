package com.example.fleetmanagement.dto;

import com.example.fleetmanagement.model.enums.ShipmentState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Primary;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryItemDto {

    private String barcode;
    private ShipmentState state;

}
