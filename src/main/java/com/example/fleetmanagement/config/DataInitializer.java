package com.example.fleetmanagement.config;

import com.example.fleetmanagement.model.Package;
import com.example.fleetmanagement.model.Sack;
import com.example.fleetmanagement.model.SackToPackage;
import com.example.fleetmanagement.model.enums.DeliveryPoint;
import com.example.fleetmanagement.model.enums.ShipmentState;
import com.example.fleetmanagement.repository.PackageRepository;
import com.example.fleetmanagement.repository.SackRepository;
import com.example.fleetmanagement.repository.SackToPackageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final PackageRepository packageRepository;
    private final SackRepository sackRepository;
    private final SackToPackageRepository sackToPackageRepository;

    @Bean
    CommandLineRunner initDatabase() {

        return args -> {
            Sack sack1 = new Sack();
            sack1.setBarcode("C725799");
            sack1.setDeliveryPoint(DeliveryPoint.DISTRIBUTION_CENTER);
            sack1.setState(ShipmentState.CREATED);
            sackRepository.save(sack1);

            Sack sack2 = new Sack();
            sack2.setBarcode("C725800");
            sack2.setDeliveryPoint(DeliveryPoint.TRANSFER_CENTER);
            sack2.setState(ShipmentState.CREATED);
            sackRepository.save(sack2);

            packageRepository.saveAll(List.of(
                    new Package("P7988000121", DeliveryPoint.BRANCH, ShipmentState.CREATED, 5),
                    new Package("P7988000122", DeliveryPoint.BRANCH, ShipmentState.CREATED, 5),
                    new Package("P7988000123", DeliveryPoint.BRANCH, ShipmentState.CREATED, 9),
                    new Package("P8988000120", DeliveryPoint.DISTRIBUTION_CENTER, ShipmentState.CREATED, 33),
                    new Package("P8988000121", DeliveryPoint.DISTRIBUTION_CENTER, ShipmentState.CREATED, 17),
                    new Package("P8988000122", DeliveryPoint.DISTRIBUTION_CENTER, ShipmentState.CREATED, 26),
                    new Package("P8988000123", DeliveryPoint.DISTRIBUTION_CENTER, ShipmentState.CREATED, 35),
                    new Package("P8988000124", DeliveryPoint.DISTRIBUTION_CENTER, ShipmentState.CREATED, 1),
                    new Package("P8988000125", DeliveryPoint.DISTRIBUTION_CENTER, ShipmentState.CREATED, 200),
                    new Package("P8988000126", DeliveryPoint.DISTRIBUTION_CENTER, ShipmentState.CREATED, 50),
                    new Package("P9988000126", DeliveryPoint.TRANSFER_CENTER, ShipmentState.CREATED, 15),
                    new Package("P9988000127", DeliveryPoint.TRANSFER_CENTER, ShipmentState.CREATED, 16),
                    new Package("P9988000128", DeliveryPoint.TRANSFER_CENTER, ShipmentState.CREATED, 55),
                    new Package("P9988000129", DeliveryPoint.TRANSFER_CENTER, ShipmentState.CREATED, 28),
                    new Package("P9988000130", DeliveryPoint.TRANSFER_CENTER, ShipmentState.CREATED, 17)
            ));

            sackToPackageRepository.saveAll(List.of(
                    new SackToPackage(sack1.getBarcode(),
                            (packageRepository.findByBarcode("P8988000122").orElseThrow()).getBarcode()),
                    new SackToPackage(sack1.getBarcode(),
                            (packageRepository.findByBarcode("P8988000126").orElseThrow()).getBarcode()),
                    new SackToPackage(sack2.getBarcode(),
                            (packageRepository.findByBarcode("P9988000128").orElseThrow()).getBarcode()),
                    new SackToPackage(sack2.getBarcode(),
                            (packageRepository.findByBarcode("P9988000129").orElseThrow()).getBarcode())
            ));

            System.out.println("Veritabanına örnek veriler eklendi.");
        };
    }
}