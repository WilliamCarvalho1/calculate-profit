import {Component} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {CalculateProfitService} from './calculate-profit.service';
import {MatButtonModule} from '@angular/material/button';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatIconModule} from '@angular/material/icon';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-calculate-profit',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule
  ],
  templateUrl: './calculate-profit.component.html',
  styleUrls: ['./calculate-profit.component.scss']
})
export class CalculateProfitComponent {
  searchForm: FormGroup;
  calculationForm: FormGroup;
  result: any = null;
  shipmentData: any = null;

  constructor(private fb: FormBuilder, private service: CalculateProfitService) {
    this.searchForm = this.fb.group({
      shipmentId: ['']
    });
    this.calculationForm = this.fb.group({
      income: ['', Validators.required],
      cost: ['', Validators.required],
      additionalCost: ['']
    });
  }

  searchShipment() {
    const shipmentId = this.searchForm.value.shipmentId;
    this.service.getShipment(shipmentId).subscribe(data => {
      this.shipmentData = data;
    });
  }

  calculateProfit() {
    const shipmentId = this.searchForm.value.shipmentId?.toString().trim();
    const calculation = this.calculationForm.value;

    // If a shipment is selected, add cargo to that shipment
    if (shipmentId) {
      this.service.createCargo(+shipmentId, calculation).subscribe(() => {
        this.refreshCargos(shipmentId);
        this.calculationForm.reset();
      });
      return;
    }

    // If no shipment is selected, create a new shipment with this cargo
    this.service.createShipment(calculation).subscribe(created => {
      // created is a CargoResponseDTO: { id, income, totalCost, profit, shipmentId }
      const newShipmentId = created.shipmentId;
      if (newShipmentId) {
        this.searchForm.patchValue({ shipmentId: newShipmentId.toString() });
        this.refreshCargos(newShipmentId);
      }
      this.calculationForm.reset();
    });
  }

  deleteCargo(cargoId: number) {
    const shipmentId = this.searchForm.value.shipmentId;
    if (!shipmentId) {
      return;
    }
    this.service.deleteCargo(cargoId).subscribe(() => {
      this.refreshCargos(shipmentId);
    });
  }

  private refreshCargos(shipmentId: string | number) {
    this.service.getShipmentWithCargos(+shipmentId).subscribe(data => {
      this.shipmentData = data;
    });
  }
}
