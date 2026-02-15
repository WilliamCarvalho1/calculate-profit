package com.studies.calculateprofit.adapter.in.controller;

import com.studies.calculateprofit.adapter.in.controller.dto.CargoRequestDTO;
import com.studies.calculateprofit.adapter.in.controller.dto.CargoResponseDTO;
import com.studies.calculateprofit.adapter.in.controller.dto.CargoUpdateRequestDTO;
import com.studies.calculateprofit.adapter.in.controller.dto.ShipmentCargosResponseDTO;
import com.studies.calculateprofit.adapter.in.controller.mapper.CargoWebMapper;
import com.studies.calculateprofit.application.port.command.UpdateCargoCommand;
import com.studies.calculateprofit.application.port.in.cargo.CreateCargoUseCase;
import com.studies.calculateprofit.application.port.in.cargo.DeleteCargoUseCase;
import com.studies.calculateprofit.application.port.in.cargo.GetCargoUseCase;
import com.studies.calculateprofit.application.port.in.cargo.UpdateCargoUseCase;
import com.studies.calculateprofit.application.port.in.shipment.CreateShipmentUseCase;
import com.studies.calculateprofit.application.port.in.shipment.DeleteShipmentUseCase;
import com.studies.calculateprofit.application.port.in.shipment.GetShipmentUseCase;
import com.studies.calculateprofit.domain.model.Cargo;
import com.studies.calculateprofit.domain.model.Shipment;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/shipments")
@SecurityRequirement(name = "bearerAuth")
public class ShipmentController {

    private final CreateShipmentUseCase createShipmentUseCase;
    private final GetShipmentUseCase getShipmentUseCase;
    private final DeleteShipmentUseCase deleteShipmentUseCase;
    private final CreateCargoUseCase createCargoUseCase;
    private final UpdateCargoUseCase updateCargoUseCase;
    private final DeleteCargoUseCase deleteCargoUseCase;
    private final GetCargoUseCase getCargoUseCase;

    public ShipmentController(CreateShipmentUseCase createShipmentUseCase, GetShipmentUseCase getShipmentUseCase,
                              DeleteShipmentUseCase deleteShipmentUseCase, CreateCargoUseCase createCargoUseCase,
                              UpdateCargoUseCase updateCargoUseCase, DeleteCargoUseCase deleteCargoUseCase,
                              GetCargoUseCase getCargoUseCase) {
        this.createShipmentUseCase = createShipmentUseCase;
        this.getShipmentUseCase = getShipmentUseCase;
        this.deleteShipmentUseCase = deleteShipmentUseCase;
        this.createCargoUseCase = createCargoUseCase;
        this.updateCargoUseCase = updateCargoUseCase;
        this.deleteCargoUseCase = deleteCargoUseCase;
        this.getCargoUseCase = getCargoUseCase;
    }

    @PostMapping
    public ResponseEntity<CargoResponseDTO> createShipment(@Valid @RequestBody CargoRequestDTO request) {
        Shipment shipment = createShipmentUseCase.createShipment();
        Cargo cargo = new Cargo(
                request.income(),
                request.cost(),
                request.additionalCost(),
                shipment
        );

        Cargo createdCargo = createCargoUseCase.createCargo(cargo);

        CargoResponseDTO response = CargoWebMapper.toResponseDTO(createdCargo);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShipmentCargosResponseDTO> getShipment(@NotNull @PathVariable Long id) {
        getShipmentUseCase.getShipment(id);
        var cargos = getCargoUseCase.findAllCargosByShipmentId(id);
        var cargoDTOlist = cargos.stream()
                .map(CargoWebMapper::toResponseDTO)
                .toList();
        var response = new ShipmentCargosResponseDTO(id, cargoDTOlist);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteShipment(@NotNull @PathVariable Long id) {
        deleteShipmentUseCase.deleteShipment(id);
    }

    @PostMapping("/{shipmentId}/cargos")
    public ResponseEntity<CargoResponseDTO> createCargo(@NotNull @PathVariable Long shipmentId,
                                                        @Valid @RequestBody CargoRequestDTO request) {
        Shipment shipment = getShipmentUseCase.getShipment(shipmentId);
        Cargo cargo = new Cargo(
                request.income(),
                request.cost(),
                request.additionalCost(),
                shipment
        );

        Cargo created = createCargoUseCase.createCargo(cargo);
        CargoResponseDTO response = CargoWebMapper.toResponseDTO(created);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/cargos/{cargoId}")
    public ResponseEntity<CargoResponseDTO> updateCargo(@NotNull @PathVariable Long cargoId,
                                                        @Valid @RequestBody CargoUpdateRequestDTO request) {
        UpdateCargoCommand command = new UpdateCargoCommand(
                Optional.ofNullable(request.income()),
                Optional.ofNullable(request.cost()),
                Optional.ofNullable(request.additionalCost())
        );

        Cargo updated = updateCargoUseCase.updateCargo(cargoId, command);
        CargoResponseDTO response = CargoWebMapper.toResponseDTO(updated);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/cargos/{cargoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCargo(@NotNull @PathVariable Long cargoId) {
        deleteCargoUseCase.deleteCargo(cargoId);
    }
}
