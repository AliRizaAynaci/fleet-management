package com.example.fleetmanagement.model.abstracts;

import com.example.fleetmanagement.model.enums.DeliveryPoint;
import com.example.fleetmanagement.model.enums.ShipmentState;
import jakarta.persistence.*;

@MappedSuperclass
public abstract class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String barcode;

    @Enumerated(EnumType.STRING)
    private DeliveryPoint deliveryPoint;

    @Enumerated(EnumType.STRING)
    private ShipmentState state;

    public Shipment(Long id, String barcode, DeliveryPoint deliveryPoint, ShipmentState state) {
        this.id = id;
        this.barcode = barcode;
        this.deliveryPoint = deliveryPoint;
        this.state = state;
    }

    protected Shipment(String barcode, DeliveryPoint deliveryPoint, ShipmentState state) {
        this.barcode = barcode;
        this.deliveryPoint = deliveryPoint;
        this.state = state;
    }

    public Shipment() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public DeliveryPoint getDeliveryPoint() {
        return deliveryPoint;
    }

    public void setDeliveryPoint(DeliveryPoint deliveryPoint) {
        this.deliveryPoint = deliveryPoint;
    }

    public ShipmentState getState() {
        return state;
    }

    public void setState(ShipmentState state) {
        this.state = state;
    }
}
