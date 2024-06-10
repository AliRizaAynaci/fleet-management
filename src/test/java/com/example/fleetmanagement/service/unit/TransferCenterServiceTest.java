package com.example.fleetmanagement.service.unit;

import com.example.fleetmanagement.model.entity.Package;
import com.example.fleetmanagement.model.entity.Sack;
import com.example.fleetmanagement.model.enums.DeliveryPoint;
import com.example.fleetmanagement.model.enums.ShipmentState;
import com.example.fleetmanagement.repository.PackageRepository;
import com.example.fleetmanagement.repository.SackRepository;
import com.example.fleetmanagement.service.impl.TransferCenterServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TransferCenterServiceTest {

    @Mock
    private PackageRepository packageRepository;
    @Mock
    private SackRepository sackRepository;

    @InjectMocks
    private TransferCenterServiceImpl transferCenterService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUnloadPackageSuccess() {
        Package packageItem = new Package("P9988000128", DeliveryPoint.TRANSFER_CENTER, 55,
                new Sack());

        when(packageRepository.findByBarcode(packageItem.getBarcode())).thenReturn(Optional.of(packageItem));
        when(packageRepository.save(any(Package.class))).thenReturn(packageItem);

        ShipmentState result = transferCenterService.unloadPackage(packageItem);

        assertEquals(ShipmentState.UNLOADED, result);
        assertEquals(null, packageItem.getSack());

        verify(packageRepository, times(1)).save(packageItem);

    }

    @Test
    void testUnloadPackage_WrongDeliveryPoint() {
        Package packageItem = new Package("P9988000128", DeliveryPoint.DISTRIBUTION_CENTER, 56,
                new Sack());

        when(packageRepository.findByBarcode(packageItem.getBarcode())).thenReturn(Optional.of(packageItem));

        assertThrows(IllegalArgumentException.class, () -> transferCenterService.unloadPackage(packageItem),
                "This package cannot be unloaded at this transfer center.");

        verify(packageRepository, never()).save(packageItem);
    }

    @Test
    void testUnloadPackage_AlreadyUnloaded() {
        Package packageItem = new Package("P9988000128", DeliveryPoint.TRANSFER_CENTER, 57,
                new Sack());
        packageItem.setState(ShipmentState.UNLOADED);

        when(packageRepository.findByBarcode(packageItem.getBarcode())).thenReturn(Optional.of(packageItem));

        assertThrows(IllegalArgumentException.class, () -> transferCenterService.unloadPackage(packageItem),
                "This package is already unloaded.");

        verify(packageRepository, never()).save(packageItem);
    }

    @Test
    void testUnloadSackSuccess() {
        Sack sack = new Sack("C725799", DeliveryPoint.TRANSFER_CENTER);
        sack.setState(ShipmentState.LOADED);

        Package packageItem = new Package("P9988000128", DeliveryPoint.TRANSFER_CENTER, 58, sack);

        sack.setPackages(List.of(packageItem));

        when(sackRepository.findById(sack.getId())).thenReturn(Optional.of(sack));
        when(packageRepository.findByBarcode(packageItem.getBarcode())).thenReturn(Optional.of(packageItem));
        when(packageRepository.save(any(Package.class))).thenReturn(packageItem);

        ShipmentState result = transferCenterService.unloadSack(sack);

        assertEquals(ShipmentState.UNLOADED, result);
        verify(sackRepository, times(1)).save(sack);
        verify(packageRepository, times(1)).save(packageItem);
    }

    @Test
    void testUnloadSack_WrongDeliveryPoint() {
        Sack sack = new Sack("C725799", DeliveryPoint.DISTRIBUTION_CENTER);
        sack.setState(ShipmentState.LOADED);

        Package packageItem = new Package("P9988000128", DeliveryPoint.TRANSFER_CENTER, 58, sack);

        sack.setPackages(List.of(packageItem));

        when(sackRepository.findById(sack.getId())).thenReturn(Optional.of(sack));

        assertThrows(IllegalArgumentException.class, () -> transferCenterService.unloadSack(sack),
                "This sack cannot be unloaded at this transfer center.");

        verify(sackRepository, never()).save(sack);
        verify(packageRepository, never()).save(packageItem);
    }

    @Test
    void testUnloadSack_AlreadyUnloaded() {
        Sack sack = new Sack("C725799", DeliveryPoint.TRANSFER_CENTER);
        sack.setState(ShipmentState.UNLOADED);

        assertThrows(IllegalArgumentException.class, () -> transferCenterService.unloadSack(sack),
                "This sack is already unloaded.");

        verify(sackRepository, never()).save(sack);
    }

    @Test
    void testUnloadSack_PackageWrongDeliveryPoint() {
        Sack sack = new Sack("C725799", DeliveryPoint.TRANSFER_CENTER);
        sack.setState(ShipmentState.LOADED);

        Package packageItem = new Package("P9988000128", DeliveryPoint.DISTRIBUTION_CENTER, 58, sack);

        sack.setPackages(List.of(packageItem));

        when(sackRepository.findById(sack.getId())).thenReturn(Optional.of(sack));

        assertThrows(IllegalArgumentException.class, () -> transferCenterService.unloadSack(sack),
                "Package with barcode " + packageItem.getBarcode() + " cannot be unloaded at this transfer center.");

        verify(sackRepository, never()).save(sack);
        verify(packageRepository, never()).save(packageItem);
    }
}
