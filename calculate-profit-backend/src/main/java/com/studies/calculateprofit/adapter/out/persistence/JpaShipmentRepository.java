package com.studies.calculateprofit.adapter.out.persistence;

import com.studies.calculateprofit.adapter.out.persistence.entity.JpaShipmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaShipmentRepository extends JpaRepository<JpaShipmentEntity, Long> {
}
