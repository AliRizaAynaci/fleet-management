package com.example.fleetmanagement.model.enums;

public enum ShipmentState {
    CREATED(1),
    LOADED_INTO_SACK(2),
    LOADED(3),
    UNLOADED(4);

    private final int value;

    ShipmentState(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
