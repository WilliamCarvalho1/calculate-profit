package com.studies.calculateprofit.infrastructure.configuration;

import com.studies.calculateprofit.application.port.in.cargo.CreateCargoUseCase;
import com.studies.calculateprofit.application.port.in.cargo.DeleteCargoUseCase;
import com.studies.calculateprofit.application.port.in.cargo.GetCargoUseCase;
import com.studies.calculateprofit.application.port.in.cargo.UpdateCargoUseCase;
import com.studies.calculateprofit.application.port.out.CargoRepositoryPort;
import com.studies.calculateprofit.application.service.CargoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CargoServiceConfig {

    @Bean
    public CargoService cargoService(CargoRepositoryPort cargoRepositoryPort) {
        return new CargoService(cargoRepositoryPort);
    }

    @Bean
    public CreateCargoUseCase createCargoUseCase(CargoService cargoService) {
        return cargoService;
    }

    @Bean
    public GetCargoUseCase getCargoUseCase(CargoService cargoService) {
        return cargoService;
    }

    @Bean
    public UpdateCargoUseCase updateCargoUseCase(CargoService cargoService) {
        return cargoService;
    }

    @Bean
    public DeleteCargoUseCase deleteCargoUseCase(CargoService cargoService) {
        return cargoService;
    }
}