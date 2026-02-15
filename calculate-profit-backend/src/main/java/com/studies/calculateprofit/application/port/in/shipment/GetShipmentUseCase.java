package com.studies.calculateprofit.application.port.in.shipment;

import com.studies.calculateprofit.domain.model.Shipment;

public interface GetShipmentUseCase {
    Shipment getShipment(Long id);
}