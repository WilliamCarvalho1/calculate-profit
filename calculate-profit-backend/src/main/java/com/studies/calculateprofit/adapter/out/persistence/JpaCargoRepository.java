package com.studies.calculateprofit.adapter.out.persistence;

import com.studies.calculateprofit.adapter.out.persistence.entity.JpaCargoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaCargoRepository extends JpaRepository<JpaCargoEntity, Long> {

    @Modifying
    @Query("delete from JpaCargoEntity c where c.shipment.id = :shipmentId")
    void deleteAllByShipmentId(@Param("shipmentId") Long shipmentId);

    @Query("select c from JpaCargoEntity c where c.shipment.id = :shipmentId")
    List<JpaCargoEntity> findAllCargosByShipmentId(@Param("shipmentId") Long shipmentId);
}
