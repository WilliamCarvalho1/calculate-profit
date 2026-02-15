package com.studies.calculateprofit.application.service;

import com.studies.calculateprofit.application.exception.DomainRuleViolationException;
import com.studies.calculateprofit.application.exception.InvalidRequestException;
import com.studies.calculateprofit.application.exception.RequestNotFoundException;
import com.studies.calculateprofit.application.port.command.UpdateCargoCommand;
import com.studies.calculateprofit.application.port.in.cargo.CreateCargoUseCase;
import com.studies.calculateprofit.application.port.in.cargo.DeleteCargoUseCase;
import com.studies.calculateprofit.application.port.in.cargo.GetCargoUseCase;
import com.studies.calculateprofit.application.port.in.cargo.UpdateCargoUseCase;
import com.studies.calculateprofit.application.port.out.CargoRepositoryPort;
import com.studies.calculateprofit.domain.exception.DomainException;
import com.studies.calculateprofit.domain.model.Cargo;

import java.util.List;

public class CargoService implements CreateCargoUseCase, GetCargoUseCase, UpdateCargoUseCase, DeleteCargoUseCase {

    private final CargoRepositoryPort repository;

    private static final String BD_ERROR_MSG = "Database error: ";

    public CargoService(CargoRepositoryPort repo) {
        this.repository = repo;
    }

    @Override
    public Cargo createCargo(Cargo cargo) {
        try {
            return repository.save(cargo);
        } catch (DomainException ex) {
            throw new DomainRuleViolationException(ex.getMessage());
        } catch (Exception ex) {
            throw new InvalidRequestException(BD_ERROR_MSG + ex.getMessage());
        }
    }

    @Override
    public Cargo getCargo(Long id) {
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
    public List<Cargo> findAllCargosByShipmentId(Long id) {
        if (id == null) {
            throw new InvalidRequestException("Shipment id must be provided.");
        }

        try {
            return repository.findAllCargosByShipmentId(id)
                    .orElse(List.of());
        } catch (Exception ex) {
            throw new InvalidRequestException(BD_ERROR_MSG + ex.getMessage());
        }
    }

    @Override
    public Cargo updateCargo(Long id, UpdateCargoCommand command) {
        try {
            Cargo retrievedCargo = getCargo(id);
            retrievedCargo.updateCargo(command);
            return repository.update(retrievedCargo);
        } catch (DomainException ex) {
            throw new DomainRuleViolationException(ex.getMessage());
        } catch (Exception ex) {
            throw new InvalidRequestException(BD_ERROR_MSG + ex.getMessage());
        }
    }

    @Override
    public void deleteCargo(Long id) {
        getCargo(id);
        try {
            repository.deleteById(id);
        } catch (Exception ex) {
            throw new InvalidRequestException(BD_ERROR_MSG + ex.getMessage());
        }
    }

    @Override
    public void deleteAllCargosByShipmentId(Long shipmentId) {
        try {
            repository.deleteAllCargosByShipmentId(shipmentId);
        } catch (Exception ex) {
            throw new InvalidRequestException(BD_ERROR_MSG + ex.getMessage());
        }
    }
}