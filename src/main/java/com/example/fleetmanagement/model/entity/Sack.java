package com.example.fleetmanagement.model.entity;

import com.example.fleetmanagement.model.abstracts.Shipment;
import com.example.fleetmanagement.model.enums.DeliveryPoint;
import com.example.fleetmanagement.model.enums.ShipmentState;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Entity
public class Sack extends Shipment {

    @OneToMany(mappedBy = "sack", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Package> packages;

    public Sack(String barcode, DeliveryPoint deliveryPoint) {
        super(barcode, deliveryPoint);
    }

    public Sack() {
    }

    public List<Package> getPackages() {
        return packages;
    }

    public void setPackages(List<Package> packages) {
        this.packages = packages;
    }
}
