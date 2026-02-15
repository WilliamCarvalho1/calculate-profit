package com.studies.calculateprofit.application.port.out;

import com.studies.calculateprofit.domain.model.Shipment;

import java.util.Optional;

public interface ShipmentRepositoryPort {
    Shipment save(Shipment shipment);

    Optional<Shipment> findById(Long id);

    void deleteById(Long id);
}