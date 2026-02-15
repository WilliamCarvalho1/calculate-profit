package com.studies.calculateprofit.adapter.out.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cargos")
public class JpaCargoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "income", nullable = false, precision = 19, scale = 2)
    public BigDecimal income;
    @Column(name = "cost", nullable = false, precision = 19, scale = 2)
    public BigDecimal cost;
    @Column(name = "additional_cost", nullable = false, precision = 19, scale = 2)
    public BigDecimal additionalCost;
    @Column(name = "total_cost", nullable = false, precision = 19, scale = 2)
    public BigDecimal totalCost;
    @Column(name = "profit", nullable = false, precision = 19, scale = 2)
    public BigDecimal profit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipment_id", nullable = false)
    private JpaShipmentEntity shipment;
}