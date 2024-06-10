package com.example.fleetmanagement.service.unit;

import com.example.fleetmanagement.model.entity.Package;
import com.example.fleetmanagement.model.entity.Sack;
import com.example.fleetmanagement.model.enums.DeliveryPoint;
import com.example.fleetmanagement.model.enums.ShipmentState;
import com.example.fleetmanagement.repository.PackageRepository;
import com.example.fleetmanagement.repository.SackRepository;
import com.example.fleetmanagement.service.impl.DistributionCenterServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class DistributionCenterServiceTest {

    @Mock
    private PackageRepository packageRepository;

    @Mock
    private SackRepository sackRepository;

    @InjectMocks
    private DistributionCenterServiceImpl distributionCenterService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testUnloadPackageSuccess() {
        Package packageItem = new Package("P7988000121", DeliveryPoint.DISTRIBUTION_CENTER, 5, null);
        when(packageRepository.findByBarcode(packageItem.getBarcode())).thenReturn(Optional.of(packageItem));
        when(packageRepository.save(any(Package.class))).thenReturn(packageItem);

        ShipmentState result = distributionCenterService.unloadPackage(packageItem);

        assertEquals(ShipmentState.UNLOADED, result);
        assertEquals(null, packageItem.getSack());
        verify(packageRepository, times(1)).save(packageItem);
    }

    @Test
    void testUnloadPackage_WrongDeliveryPoint() {
        Package packageItem = new Package("P8988000120", DeliveryPoint.BRANCH, 33, new Sack());
        when(packageRepository.findByBarcode(packageItem.getBarcode())).thenReturn(Optional.of(packageItem));

        assertThrows(IllegalArgumentException.class, () ->
                        distributionCenterService.unloadPackage(packageItem),
                "This package cannot be unloaded at this distribution center.");

        verify(packageRepository, never()).save(packageItem);
    }

    @Test
    void testUnloadPackage_AlreadyUnloaded() {
        Package packageItem = new Package("P8988000120", DeliveryPoint.DISTRIBUTION_CENTER, 33, new Sack());
        packageItem.setState(ShipmentState.UNLOADED);

        when(packageRepository.findByBarcode(packageItem.getBarcode())).thenReturn(Optional.of(packageItem));

        assertThrows(IllegalArgumentException.class, () ->
                distributionCenterService.unloadPackage(packageItem),
                "This package is already unloaded.");

        verify(packageRepository, never()).save(packageItem);
    }

    @Test
    void testUnloadSackSuccess() {
        Sack sack = new Sack("C725799", DeliveryPoint.DISTRIBUTION_CENTER);
        sack.setState(ShipmentState.LOADED);

        Package packageItem = new Package("P8988000120", DeliveryPoint.DISTRIBUTION_CENTER, 33, new Sack());
        packageItem.setState(ShipmentState.LOADED);

        sack.setPackages(List.of(packageItem));

        when(sackRepository.findById(sack.getId())).thenReturn(Optional.of(sack));
        when(sackRepository.save(any(Sack.class))).thenReturn(sack);

        ShipmentState result = distributionCenterService.unloadSack(sack);

        assertEquals(ShipmentState.UNLOADED, result);
        verify(sackRepository, times(1)).save(sack);
        verify(packageRepository, times(1)).save(packageItem);
    }

    @Test
    void testUnloadSack_WrongDeliveryPoint() {
        Sack sack = new Sack("C725799", DeliveryPoint.BRANCH);
        sack.setState(ShipmentState.LOADED);

        assertThrows(IllegalArgumentException.class, () ->
                        distributionCenterService.unloadSack(sack),
                "This sack cannot be unloaded at this distribution center.");

        verify(sackRepository, never()).save(sack);
    }

    @Test
    void testUnloadSack_AlreadyUnloaded() {
        Sack sack = new Sack("C725799", DeliveryPoint.DISTRIBUTION_CENTER);
        sack.setState(ShipmentState.UNLOADED);

        when(sackRepository.findById(sack.getId())).thenReturn(Optional.of(sack));

        assertThrows(IllegalArgumentException.class, () ->
                        distributionCenterService.unloadSack(sack),
                "This sack is already unloaded.");

        verify(sackRepository, never()).save(sack);
    }

    @Test
    void testUnloadSack_SackNotFound() {
        Sack sack = new Sack("C725799", DeliveryPoint.DISTRIBUTION_CENTER);
        sack.setState(ShipmentState.LOADED);

        when(sackRepository.findById(sack.getId())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                        distributionCenterService.unloadSack(sack),
                "Sack not found.");

        verify(sackRepository, never()).save(sack);
    }

}
