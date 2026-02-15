package com.studies.calculateprofit.application.port.in.cargo;

import com.studies.calculateprofit.domain.model.Cargo;

import java.util.List;

public interface GetCargoUseCase {

    Cargo getCargo(Long id);

    List<Cargo> findAllCargosByShipmentId(Long id);

}