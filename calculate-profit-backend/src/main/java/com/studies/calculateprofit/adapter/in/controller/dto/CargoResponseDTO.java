package com.studies.calculateprofit.adapter.in.controller.dto;

import java.math.BigDecimal;

public record CargoResponseDTO(
        Long id,
        BigDecimal income,
        BigDecimal totalCost,
        BigDecimal profit,
        Long shipmentId
) {
}
