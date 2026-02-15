package com.studies.calculateprofit.adapter.in.controller.dto;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CargoUpdateRequestDTO(
        @Positive(message = "income must be positive")
        BigDecimal income,

        @Positive(message = "cost must be positive")
        BigDecimal cost,

        @Positive(message = "additionalCost must be positive")
        BigDecimal additionalCost
) {
}
