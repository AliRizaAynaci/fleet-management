package com.example.fleetmanagement.model.enums;

public enum DeliveryPoint {
    BRANCH(1),
    DISTRIBUTION_CENTER(2),
    TRANSFER_CENTER(3);

    private final int value;

    DeliveryPoint(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
