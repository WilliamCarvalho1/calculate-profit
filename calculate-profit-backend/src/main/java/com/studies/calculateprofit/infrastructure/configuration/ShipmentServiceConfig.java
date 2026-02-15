package com.studies.calculateprofit.infrastructure.configuration;

import com.studies.calculateprofit.application.port.in.cargo.DeleteCargoUseCase;
import com.studies.calculateprofit.application.port.in.shipment.CreateShipmentUseCase;
import com.studies.calculateprofit.application.port.in.shipment.DeleteShipmentUseCase;
import com.studies.calculateprofit.application.port.in.shipment.GetShipmentUseCase;
import com.studies.calculateprofit.application.port.out.ShipmentRepositoryPort;
import com.studies.calculateprofit.application.service.ShipmentService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShipmentServiceConfig {

    @Bean
    public ShipmentService shipmentService(ShipmentRepositoryPort shipmentRepositoryPort,
                                           DeleteCargoUseCase deleteCargoUseCase) {
        return new ShipmentService(shipmentRepositoryPort, deleteCargoUseCase);
    }

    @Bean
    public CreateShipmentUseCase createShipmentUseCase(ShipmentService shipmentService) {
        return shipmentService;
    }

    @Bean
    public GetShipmentUseCase getShipmentUseCase(ShipmentService shipmentService) {
        return shipmentService;
    }

    @Bean
    public DeleteShipmentUseCase deleteShipmentUseCase(ShipmentService shipmentService) {
        return shipmentService;
    }
}