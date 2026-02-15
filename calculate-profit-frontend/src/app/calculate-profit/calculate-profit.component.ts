import {Component} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {CalculateProfitService} from './calculate-profit.service';
import {MatButtonModule} from '@angular/material/button';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-calculate-profit',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule
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
      shipmentId: ['', Validators.required]
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
    const shipmentId = this.searchForm.value.shipmentId;
    const calculation = this.calculationForm.value;
    this.service.createCargo(shipmentId, calculation).subscribe(result => {
      // After creating cargo, refresh shipment data to update cargos table
      this.service.getShipmentWithCargos(shipmentId).subscribe(data => {
        this.shipmentData = data;
        this.result = null;
        this.calculationForm.reset();
      });
    });
  }
}
