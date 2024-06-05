package com.example.fleetmanagement.repository;

import com.example.fleetmanagement.model.entity.Sack;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SackRepository extends JpaRepository<Sack, Long> {

    Optional<Sack> findByBarcode(String barcode);
}
