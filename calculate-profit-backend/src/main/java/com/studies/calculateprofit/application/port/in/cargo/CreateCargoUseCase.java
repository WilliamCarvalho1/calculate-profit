package com.studies.calculateprofit.application.port.in.cargo;

import com.studies.calculateprofit.domain.model.Cargo;

public interface CreateCargoUseCase {

    Cargo createCargo(Cargo cargo);
}