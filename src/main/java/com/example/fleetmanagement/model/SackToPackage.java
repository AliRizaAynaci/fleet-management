package com.example.fleetmanagement.model;

import jakarta.persistence.*;

@Entity
public class SackToPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sackBarcode;

    private String packageBarcode;

    public SackToPackage(String sackBarcode, String packageBarcode) {
        this.sackBarcode = sackBarcode;
        this.packageBarcode = packageBarcode;
    }

    public SackToPackage() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSackBarcode() {
        return sackBarcode;
    }

    public void setSackBarcode(String sackBarcode) {
        this.sackBarcode = sackBarcode;
    }

    public String getPackageBarcode() {
        return packageBarcode;
    }

    public void setPackageBarcode(String packageBarcode) {
        this.packageBarcode = packageBarcode;
    }
}
