package com.studies.calculateprofit.application.service;

import com.studies.calculateprofit.application.exception.DomainRuleViolationException;
import com.studies.calculateprofit.application.exception.InvalidRequestException;
import com.studies.calculateprofit.application.exception.RequestNotFoundException;
import com.studies.calculateprofit.application.port.command.UpdateCargoCommand;
import com.studies.calculateprofit.application.port.out.CargoRepositoryPort;
import com.studies.calculateprofit.domain.model.Cargo;
import com.studies.calculateprofit.domain.model.Shipment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CargoServiceTest {

    private CargoRepositoryPort repository;
    private CargoService service;

    @BeforeEach
    void setUp() {
        repository = mock(CargoRepositoryPort.class);
        service = new CargoService(repository);
    }

    @Test
    void getCargoThrowsInvalidRequestWhenIdNull() {
        InvalidRequestException ex = assertThrows(InvalidRequestException.class, () -> service.getCargo(null));
        assertTrue(ex.getMessage().contains("Shipment id must be provided"));
    }

    @Test
    void getCargoThrowsRequestNotFoundWhenMissing() {
        when(repository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(RequestNotFoundException.class, () -> service.getCargo(1L));
    }

    @Test
    void getCargoWrapsExceptionAsInvalidRequestDbError() {
        when(repository.findById(1L))
                .thenThrow(new RuntimeException("boom"));

        InvalidRequestException ex = assertThrows(InvalidRequestException.class, () -> service.getCargo(1L));
        assertTrue(ex.getMessage().startsWith("Database error:"));
    }

    @Test
    void updateCargoThrowsDomainRuleViolationWhenDomainException() {
        Shipment shipment = new Shipment(1L);
        Cargo existing = new Cargo(new BigDecimal("100"), new BigDecimal("10"), new BigDecimal("5"), shipment);

        when(repository.findById(1L))
                .thenReturn(Optional.of(existing));

        UpdateCargoCommand command = new UpdateCargoCommand(
                Optional.of(BigDecimal.ZERO),
                Optional.empty(),
                Optional.empty()
        );

        assertThrows(DomainRuleViolationException.class, () -> service.updateCargo(1L, command));
    }

    @Test
    void findAllCargosByShipmentIdReturnsEmptyListWhenNone() {
        when(repository.findAllCargosByShipmentId(1L))
                .thenReturn(Optional.of(List.of()));

        List<Cargo> result = service.findAllCargosByShipmentId(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
