package com.studies.calculateprofit.adapter.in.controller.mapper;

import com.studies.calculateprofit.adapter.in.controller.dto.CargoResponseDTO;
import com.studies.calculateprofit.domain.model.Cargo;

public class CargoWebMapper {

    private CargoWebMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static CargoResponseDTO toResponseDTO(Cargo cargo) {
        return new CargoResponseDTO(
                cargo.getId(),
                cargo.getIncome(),
                cargo.getTotalCost(),
                cargo.getProfit(),
                cargo.getShipment().getId()
        );
    }
}
