package com.studies.calculateprofit.application.port.out;

import com.studies.calculateprofit.domain.model.Cargo;

import java.util.List;
import java.util.Optional;

public interface CargoRepositoryPort {
    Cargo save(Cargo cargo);

    Optional<Cargo> findById(Long id);

    Optional<List<Cargo>> findAllCargosByShipmentId(Long shipmentId);

    Cargo update(Cargo updatedCargo);

    void deleteById(Long id);

    void deleteAllCargosByShipmentId(Long id);
}