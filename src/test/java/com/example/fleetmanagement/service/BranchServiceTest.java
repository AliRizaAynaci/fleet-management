package com.example.fleetmanagement.service;

import com.example.fleetmanagement.model.entity.Package;
import com.example.fleetmanagement.model.entity.Sack;
import com.example.fleetmanagement.model.enums.DeliveryPoint;
import com.example.fleetmanagement.model.enums.ShipmentState;
import com.example.fleetmanagement.repository.PackageRepository;
import com.example.fleetmanagement.service.impl.BranchServiceImpl;
import com.example.fleetmanagement.service.interfaces.BranchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BranchServiceTest {

    @Mock
    private PackageRepository packageRepository;
    @InjectMocks
    private BranchServiceImpl branchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUnloadPackageSuccess() {
        Package packageItem = new Package("P7988000121", DeliveryPoint.BRANCH, 5, null);
        when(packageRepository.findByBarcode(packageItem.getBarcode())).thenReturn(Optional.of(packageItem));
        when(packageRepository.save(any(Package.class))).thenReturn(packageItem);

        ShipmentState result = branchService.unloadPackage(packageItem);

        assertEquals(ShipmentState.UNLOADED, result);
        assertEquals(null, packageItem.getSack());
        verify(packageRepository, times(1)).save(packageItem);
    }

    @Test
    void testUnloadPackage_WrongDeliveryPoint() {
        Package packageItem = new Package("P8988000120", DeliveryPoint.DISTRIBUTION_CENTER, 33, null);
        when(packageRepository.findByBarcode(packageItem.getBarcode())).thenReturn(Optional.of(packageItem));

        assertThrows(IllegalArgumentException.class, () -> branchService.unloadPackage(packageItem),
                "This package cannot be unloaded at this branch.");
        verify(packageRepository, never()).save(packageItem);
    }

    @Test
    void testUnloadPackage_InSack() {
        Package packageItem = new Package("P8988000122", DeliveryPoint.BRANCH, 26, new Sack());
        when(packageRepository.findByBarcode(packageItem.getBarcode())).thenReturn(Optional.of(packageItem));

        assertThrows(IllegalArgumentException.class, () -> branchService.unloadPackage(packageItem), // BranchServiceImpl'i kullan
                "This package is in a sack. It must be unloaded at a Distribute Center or Transfer Center.");
        verify(packageRepository, never()).save(packageItem);
    }

}
