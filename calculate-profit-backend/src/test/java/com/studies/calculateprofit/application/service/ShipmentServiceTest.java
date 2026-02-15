package com.studies.calculateprofit.application.service;

import com.studies.calculateprofit.application.exception.InvalidRequestException;
import com.studies.calculateprofit.application.exception.RequestNotFoundException;
import com.studies.calculateprofit.application.port.in.cargo.DeleteCargoUseCase;
import com.studies.calculateprofit.application.port.out.ShipmentRepositoryPort;
import com.studies.calculateprofit.domain.model.Shipment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class ShipmentServiceTest {

    private ShipmentRepositoryPort repository;
    private DeleteCargoUseCase deleteCargoUseCase;
    private ShipmentService service;

    @BeforeEach
    void setUp() {
        repository = mock(ShipmentRepositoryPort.class);
        deleteCargoUseCase = mock(DeleteCargoUseCase.class);
        service = new ShipmentService(repository, deleteCargoUseCase);
    }

    @Test
    void getShipmentThrowsInvalidRequestWhenIdNull() {
        InvalidRequestException ex = assertThrows(InvalidRequestException.class, () -> service.getShipment(null));

        assertTrue(ex.getMessage().contains("Shipment id must be provided"));
    }

    @Test
    void getShipmentThrowsRequestNotFoundWhenMissing() {
        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(RequestNotFoundException.class, () -> service.getShipment(1L));
    }

    @Test
    void deleteShipmentDeletesCargosThenShipment() {
        Shipment shipment = new Shipment(1L);
        when(repository.findById(1L))
                .thenReturn(Optional.of(shipment));

        service.deleteShipment(1L);

        verify(deleteCargoUseCase).deleteAllCargosByShipmentId(1L);
        verify(repository).deleteById(1L);
    }

    @Test
    void deleteShipmentWrapsDataAccessExceptionAsInvalidRequestDbError() {
        Shipment shipment = new Shipment(1L);
        when(repository.findById(1L))
                .thenReturn(Optional.of(shipment));
        doThrow(new DataAccessException("boom") {
        }).when(repository).deleteById(1L);

        InvalidRequestException ex = assertThrows(InvalidRequestException.class, () -> service.deleteShipment(1L));
        assertTrue(ex.getMessage().startsWith("Database error:"));
    }
}

