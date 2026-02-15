package com.studies.calculateprofit.domain.model;

import com.studies.calculateprofit.domain.exception.DomainException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CargoTest {

    @Test
    void createsCargoAndCalculatesTotalsAndProfit() {
        Shipment shipment = new Shipment(1L);

        Cargo cargo = new Cargo(
                new BigDecimal("2500"),
                new BigDecimal("600"),
                new BigDecimal("30"),
                shipment
        );

        assertEquals(new BigDecimal("630"), cargo.getTotalCost());
        assertEquals(new BigDecimal("1870"), cargo.getProfit());
    }

    @Test
    void changeIncomeWithNonPositiveValueThrowsDomainException() {
        Shipment shipment = new Shipment(1L);
        Cargo cargo = new Cargo(
                new BigDecimal("100"),
                new BigDecimal("50"),
                new BigDecimal("10"),
                shipment
        );

        assertThrows(DomainException.class, () -> cargo.changeIncome(new BigDecimal("0")));
        assertThrows(DomainException.class, () -> cargo.changeIncome(new BigDecimal("-1")));
    }

    @Test
    void changeCostWithNonPositiveValueThrowsDomainException() {
        Shipment shipment = new Shipment(1L);
        Cargo cargo = new Cargo(
                new BigDecimal("100"),
                new BigDecimal("50"),
                new BigDecimal("10"),
                shipment
        );

        assertThrows(DomainException.class, () -> cargo.changeCost(new BigDecimal("0")));
        assertThrows(DomainException.class, () -> cargo.changeCost(new BigDecimal("-1")));
    }

    @Test
    void changeAdditionalCostWithNonPositiveValueThrowsDomainException() {
        Shipment shipment = new Shipment(1L);
        Cargo cargo = new Cargo(
                new BigDecimal("100"),
                new BigDecimal("50"),
                new BigDecimal("10"),
                shipment
        );

        assertThrows(DomainException.class, () -> cargo.changeAdditionalCost(new BigDecimal("0")));
        assertThrows(DomainException.class, () -> cargo.changeAdditionalCost(new BigDecimal("-1")));
    }
}
