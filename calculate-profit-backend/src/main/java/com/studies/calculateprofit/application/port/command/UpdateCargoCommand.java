package com.studies.calculateprofit.application.port.command;

import java.math.BigDecimal;
import java.util.Optional;

public record UpdateCargoCommand(
        Optional<BigDecimal> income,
        Optional<BigDecimal> cost,
        Optional<BigDecimal> additionalCost
) {
}
