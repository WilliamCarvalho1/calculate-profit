package com.studies.calculateprofit.adapter.in.controller.dto;

import java.util.List;

public record ShipmentCargosResponseDTO(
        Long shipmentId,
        List<CargoResponseDTO> cargos
) {
}