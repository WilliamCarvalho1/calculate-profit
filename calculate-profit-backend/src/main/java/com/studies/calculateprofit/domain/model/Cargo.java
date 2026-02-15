package com.studies.calculateprofit.domain.model;

import com.studies.calculateprofit.application.port.command.UpdateCargoCommand;
import com.studies.calculateprofit.domain.exception.DomainException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class Cargo {
    private Long id;
    private BigDecimal income;
    private BigDecimal cost;
    private BigDecimal additionalCost;
    private BigDecimal totalCost;
    private BigDecimal profit;
    private Shipment shipment;

    public Cargo(BigDecimal income, BigDecimal cost, BigDecimal additionalCost, Shipment shipment) {
        changeShipment(shipment);
        changeIncome(income);
        changeCost(cost);
        changeAdditionalCost(additionalCost);
        recalculateTotals();
    }

    public void updateCargo(UpdateCargoCommand update) {
        update.income().ifPresent(this::changeIncome);
        update.cost().ifPresent(this::changeCost);
        update.additionalCost().ifPresent(this::changeAdditionalCost);
        recalculateTotals();
    }

    public void changeIncome(BigDecimal income) {
        if (income == null) {
            throw new DomainException("Income must not be null.");
        }
        if (income.signum() <= 0) {
            throw new DomainException("Income must be positive.");
        }
        this.income = income;
    }

    public void changeCost(BigDecimal cost) {
        if (cost == null) {
            throw new DomainException("Cost must not be null.");
        }
        if (cost.signum() <= 0) {
            throw new DomainException("Cost must be positive.");
        }
        this.cost = cost;
    }

    public void changeAdditionalCost(BigDecimal additionalCost) {
        if (additionalCost == null) {
            this.additionalCost = BigDecimal.valueOf(0);
            return;
        }
        if (additionalCost.signum() <= 0) {
            throw new DomainException("Additional Cost must be positive.");
        }
        this.additionalCost = additionalCost;
    }

    private void recalculateTotals() {
        this.totalCost = cost.add(additionalCost);
        this.profit = income.subtract(totalCost);
    }

    public void changeShipment(Shipment shipment) {
        if (shipment == null) {
            throw new DomainException("Shipment must not be null.");
        }
        this.shipment = shipment;
    }
}