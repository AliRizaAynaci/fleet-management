package com.example.fleetmanagement.service.impl;

import com.example.fleetmanagement.model.Package;
import com.example.fleetmanagement.model.Sack;
import com.example.fleetmanagement.model.enums.DeliveryPoint;
import com.example.fleetmanagement.model.enums.ShipmentState;
import com.example.fleetmanagement.repository.PackageRepository;
import com.example.fleetmanagement.repository.SackRepository;
import com.example.fleetmanagement.service.interfaces.TransferCenterService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransferCenterServiceImpl implements TransferCenterService {

    private final PackageRepository packageRepository;
    private final SackRepository sackRepository;

    public TransferCenterServiceImpl(PackageRepository packageRepository, SackRepository sackRepository) {
        this.packageRepository = packageRepository;
        this.sackRepository = sackRepository;
    }

    @Override
    public ShipmentState unloadSack(Sack sack) {
        if (sack.getDeliveryPoint() != DeliveryPoint.TRANSFER_CENTER) {
            throw new IllegalArgumentException("This sack cannot be unloaded at this transfer center.");
        }
        if (sack.getState() == ShipmentState.UNLOADED) {
            throw new IllegalArgumentException("This sack is already unloaded.");
        }

        sack = sackRepository.findById(sack.getId()).orElseThrow(() -> new RuntimeException("Sack not found"));

        List<Package> packages = sack.getPackages();
        for (Package packageItem : packages) {
            if (packageItem.getDeliveryPoint() != DeliveryPoint.TRANSFER_CENTER) {
                throw new IllegalArgumentException("Package with barcode " + packageItem.getBarcode()
                        + " cannot be unloaded at this transfer center.");
            }
            packageItem.setState(ShipmentState.UNLOADED);
            packageItem.setSack(null);
            packageRepository.save(packageItem);
        }

        sack.setState(ShipmentState.UNLOADED);
        sackRepository.save(sack);
        return ShipmentState.UNLOADED;
    }
}
