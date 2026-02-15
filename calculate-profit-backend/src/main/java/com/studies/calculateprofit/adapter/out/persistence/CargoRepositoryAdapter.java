package com.studies.calculateprofit.adapter.out.persistence;

import com.studies.calculateprofit.adapter.out.persistence.entity.JpaCargoEntity;
import com.studies.calculateprofit.adapter.out.persistence.mapper.CargoPersistenceMapper;
import com.studies.calculateprofit.application.port.out.CargoRepositoryPort;
import com.studies.calculateprofit.domain.model.Cargo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CargoRepositoryAdapter implements CargoRepositoryPort {

    private final JpaCargoRepository repository;

    public CargoRepositoryAdapter(JpaCargoRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public Cargo save(Cargo cargo) {
        JpaCargoEntity entity = CargoPersistenceMapper.toJpaEntity(cargo);
        JpaCargoEntity savedEntity = repository.save(entity);
        return CargoPersistenceMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Cargo> findById(Long id) {
        return repository.findById(id)
                .map(CargoPersistenceMapper::toDomain);
    }

    @Override
    public Optional<List<Cargo>> findAllCargosByShipmentId(Long shipmentId) {
        List<JpaCargoEntity> entities = repository.findAllCargosByShipmentId(shipmentId);
        return Optional.of(CargoPersistenceMapper.toDomainList(entities));
    }

    @Override
    @Transactional
    public Cargo update(Cargo retrievedCargo) {
        // Load existing JPA entity to keep the managed shipment association
        JpaCargoEntity existing = repository.findById(retrievedCargo.getId())
                .orElseThrow(() -> new IllegalArgumentException("Cargo not found: " + retrievedCargo.getId()));

        existing.income = retrievedCargo.getIncome();
        existing.cost = retrievedCargo.getCost();
        existing.additionalCost = retrievedCargo.getAdditionalCost();
        existing.totalCost = retrievedCargo.getTotalCost();
        existing.profit = retrievedCargo.getProfit();

        JpaCargoEntity savedEntity = repository.save(existing);
        return CargoPersistenceMapper.toDomain(savedEntity);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteAllCargosByShipmentId(Long id) {
        repository.deleteAllByShipmentId(id);
    }

}