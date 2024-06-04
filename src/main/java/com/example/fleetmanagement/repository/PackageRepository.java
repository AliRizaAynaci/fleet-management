package com.example.fleetmanagement.repository;

import com.example.fleetmanagement.model.Package;
import com.example.fleetmanagement.model.enums.DeliveryPoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PackageRepository extends JpaRepository<Package, Long> {
    List<Package> findByDeliveryPoint(DeliveryPoint deliveryPoint);

    Optional<Package> findByBarcode(String p8988000122);
}
