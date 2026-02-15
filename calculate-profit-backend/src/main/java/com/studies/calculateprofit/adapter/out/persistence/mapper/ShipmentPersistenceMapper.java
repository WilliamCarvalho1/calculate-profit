package com.studies.calculateprofit.adapter.out.persistence.mapper;

import com.studies.calculateprofit.adapter.out.persistence.entity.JpaShipmentEntity;
import com.studies.calculateprofit.domain.model.Shipment;

import java.util.List;

public class ShipmentPersistenceMapper {

    private ShipmentPersistenceMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static Shipment toDomain(JpaShipmentEntity jpaShipmentEntity) {
        return new Shipment(jpaShipmentEntity.id);
    }

    public static JpaShipmentEntity toJpaEntity(Shipment shipment) {
        return new JpaShipmentEntity(shipment.getId());
    }

    public static List<Shipment> toDomainList(List<JpaShipmentEntity> entities) {
        return entities.stream()
                .map(ShipmentPersistenceMapper::toDomain)
                .toList();
    }
}