package com.studies.calculateprofit.adapter.in.controller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CargoRequestDTO(
        @NotNull(message = "income must not be null")
        @Positive(message = "income must be positive")
        BigDecimal income,

        @NotNull(message = "cost must not be null")
        @Positive(message = "cost must be positive")
        BigDecimal cost,

        @NotNull(message = "additionalCost must not be null")
        @Positive(message = "additionalCost must be positive")
        BigDecimal additionalCost
) {
}