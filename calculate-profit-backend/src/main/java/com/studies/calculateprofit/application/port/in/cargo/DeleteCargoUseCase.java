package com.studies.calculateprofit.application.port.in.cargo;

public interface DeleteCargoUseCase {

    void deleteCargo(Long id);

    void deleteAllCargosByShipmentId(Long shipmentId);
}
