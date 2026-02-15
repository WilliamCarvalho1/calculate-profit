package com.studies.calculateprofit.application.service;

import com.studies.calculateprofit.application.exception.InvalidRequestException;
import com.studies.calculateprofit.application.exception.RequestNotFoundException;
import com.studies.calculateprofit.application.port.in.cargo.DeleteCargoUseCase;
import com.studies.calculateprofit.application.port.in.shipment.CreateShipmentUseCase;
import com.studies.calculateprofit.application.port.in.shipment.DeleteShipmentUseCase;
import com.studies.calculateprofit.application.port.in.shipment.GetShipmentUseCase;
import com.studies.calculateprofit.application.port.out.ShipmentRepositoryPort;
import com.studies.calculateprofit.domain.model.Shipment;

public class ShipmentService implements CreateShipmentUseCase, GetShipmentUseCase, DeleteShipmentUseCase {

    private final ShipmentRepositoryPort repository;
    private final DeleteCargoUseCase deleteCargoUseCase;

    private static final String BD_ERROR_MSG = "Database error: ";

    public ShipmentService(ShipmentRepositoryPort repo, DeleteCargoUseCase deleteCargoUseCase) {
        this.repository = repo;
        this.deleteCargoUseCase = deleteCargoUseCase;
    }

    @Override
    public Shipment createShipment() {
        try {
            return repository.save(new Shipment());
        } catch (Exception ex) {
            throw new InvalidRequestException(BD_ERROR_MSG + ex.getMessage());
        }
    }

    @Override
    public Shipment getShipment(Long id) {
        if (id == null) {
            throw new InvalidRequestException("Shipment id must be provided.");
        }

        try {
            return repository.findById(id)
                    .orElseThrow(() -> new RequestNotFoundException(id));
        } catch (RequestNotFoundException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new InvalidRequestException(BD_ERROR_MSG + ex.getMessage());
        }
    }

    @Override
    public void deleteShipment(Long id) {
        Shipment retrievedShipment = getShipment(id);

        try {
            // First delete all cargos referencing this shipment to satisfy FK constraint
            deleteCargoUseCase.deleteAllCargosByShipmentId(retrievedShipment.getId());
            // Then delete the shipment itself
            repository.deleteById(retrievedShipment.getId());
        } catch (Exception ex) {
            throw new InvalidRequestException(BD_ERROR_MSG + ex.getMessage());
        }
    }
}