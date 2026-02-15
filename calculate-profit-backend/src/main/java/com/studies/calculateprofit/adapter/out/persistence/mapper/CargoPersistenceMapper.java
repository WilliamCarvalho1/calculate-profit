package com.studies.calculateprofit.adapter.out.persistence.mapper;

import com.studies.calculateprofit.adapter.out.persistence.entity.JpaCargoEntity;
import com.studies.calculateprofit.domain.model.Cargo;

import java.util.List;

public class CargoPersistenceMapper {

    private CargoPersistenceMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static Cargo toDomain(JpaCargoEntity jpaEntity) {
        return new Cargo(
                jpaEntity.id,
                jpaEntity.income,
                jpaEntity.cost,
                jpaEntity.additionalCost,
                jpaEntity.totalCost,
                jpaEntity.profit,
                ShipmentPersistenceMapper.toDomain(jpaEntity.getShipment())
        );
    }

    public static JpaCargoEntity toJpaEntity(Cargo response) {
        return new JpaCargoEntity(
                response.getId(),
                response.getIncome(),
                response.getCost(),
                response.getAdditionalCost(),
                response.getTotalCost(),
                response.getProfit(),
                ShipmentPersistenceMapper.toJpaEntity(response.getShipment())
        );
    }

    public static List<Cargo> toDomainList(List<JpaCargoEntity> entities) {
        return entities.stream()
                .map(CargoPersistenceMapper::toDomain)
                .toList();
    }
}