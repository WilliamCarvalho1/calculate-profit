package com.studies.calculateprofit.application.port.in.cargo;

import com.studies.calculateprofit.application.port.command.UpdateCargoCommand;
import com.studies.calculateprofit.domain.model.Cargo;

public interface UpdateCargoUseCase {

    Cargo updateCargo(Long id, UpdateCargoCommand command);

}
