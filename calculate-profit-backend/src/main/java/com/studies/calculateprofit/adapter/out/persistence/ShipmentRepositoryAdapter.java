package com.studies.calculateprofit.adapter.out.persistence;

import com.studies.calculateprofit.adapter.out.persistence.entity.JpaShipmentEntity;
import com.studies.calculateprofit.adapter.out.persistence.mapper.ShipmentPersistenceMapper;
import com.studies.calculateprofit.application.port.out.ShipmentRepositoryPort;
import com.studies.calculateprofit.domain.model.Shipment;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ShipmentRepositoryAdapter implements ShipmentRepositoryPort {

    private final JpaShipmentRepository repository;

    public ShipmentRepositoryAdapter(JpaShipmentRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Shipment save(Shipment shipment) {
        JpaShipmentEntity entity = ShipmentPersistenceMapper.toJpaEntity(shipment);

        JpaShipmentEntity savedEntity = repository.save(entity);

        return ShipmentPersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Shipment> findById(Long id) {
        return repository.findById(id)
                .map(ShipmentPersistenceMapper::toDomain);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}