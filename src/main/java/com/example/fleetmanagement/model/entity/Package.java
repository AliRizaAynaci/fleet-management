package com.example.fleetmanagement.model.entity;

import com.example.fleetmanagement.model.abstracts.Shipment;
import com.example.fleetmanagement.model.enums.DeliveryPoint;
import com.example.fleetmanagement.model.enums.ShipmentState;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Package extends Shipment {

    private Integer desi;

    @ManyToOne
    @JoinColumn(name = "sack_id")
    private Sack sack;

    public Package(String barcode, DeliveryPoint deliveryPoint,
                   ShipmentState state, Integer desi, Sack sack) {
        super(barcode, deliveryPoint, state);
        this.desi = desi;
        this.sack = sack;
    }

    public Package() {

    }

    public Integer getDesi() {
        return desi;
    }

    public void setDesi(Integer desi) {
        this.desi = desi;
    }

    public Sack getSack() {
        return sack;
    }

    public void setSack(Sack sack) {
        this.sack = sack;
    }
}
