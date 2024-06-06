package com.example.fleetmanagement.config;

import com.example.fleetmanagement.model.entity.Package;
import com.example.fleetmanagement.model.entity.Sack;
import com.example.fleetmanagement.model.enums.DeliveryPoint;
import com.example.fleetmanagement.model.enums.ShipmentState;
import com.example.fleetmanagement.repository.PackageRepository;
import com.example.fleetmanagement.repository.SackRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final PackageRepository packageRepository;
    private final SackRepository sackRepository;

    @Bean
    CommandLineRunner initDatabase() {

        return args -> {
            Sack sack1 = new Sack();
            sack1.setBarcode("C725799");
            sack1.setDeliveryPoint(DeliveryPoint.DISTRIBUTION_CENTER);
            sack1.setState(ShipmentState.LOADED);
            sackRepository.save(sack1);

            Sack sack2 = new Sack();
            sack2.setBarcode("C725800");
            sack2.setDeliveryPoint(DeliveryPoint.TRANSFER_CENTER);
            sack2.setState(ShipmentState.LOADED);
            sackRepository.save(sack2);

            packageRepository.saveAll(List.of(
                    new Package("P7988000121", DeliveryPoint.BRANCH, 5, null),
                    new Package("P7988000122", DeliveryPoint.BRANCH, 5, null),
                    new Package("P7988000123", DeliveryPoint.BRANCH, 9, null),
                    new Package("P8988000120", DeliveryPoint.DISTRIBUTION_CENTER, 33, null),
                    new Package("P8988000121", DeliveryPoint.DISTRIBUTION_CENTER, 17, null),
                    new Package("P8988000122", DeliveryPoint.DISTRIBUTION_CENTER, 26, sack1),
                    new Package("P8988000123", DeliveryPoint.DISTRIBUTION_CENTER, 35, null),
                    new Package("P8988000124", DeliveryPoint.DISTRIBUTION_CENTER, 1, null),
                    new Package("P8988000125", DeliveryPoint.DISTRIBUTION_CENTER, 200, null),
                    new Package("P8988000126", DeliveryPoint.DISTRIBUTION_CENTER, 50, sack1),
                    new Package("P9988000126", DeliveryPoint.TRANSFER_CENTER, 15, null),
                    new Package("P9988000127", DeliveryPoint.TRANSFER_CENTER, 16, null),
                    new Package("P9988000128", DeliveryPoint.TRANSFER_CENTER, 55, sack2),
                    new Package("P9988000129", DeliveryPoint.TRANSFER_CENTER, 28, sack2),
                    new Package("P9988000130", DeliveryPoint.TRANSFER_CENTER, 17, null)
            ));

            System.out.println("Veritabanına örnek veriler eklendi.");
        };
    }
}