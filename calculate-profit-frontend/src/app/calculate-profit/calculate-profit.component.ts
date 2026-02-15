import {Component} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {CalculateProfitService} from './calculate-profit.service';
import {MatButtonModule} from '@angular/material/button';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatIconModule} from '@angular/material/icon';
import {CommonModule} from '@angular/common';
import {HttpErrorResponse} from '@angular/common/http';

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
  errorMessage: string | null = null;

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

  private buildErrorMessage(context: string, error: unknown): string {
    if (error instanceof HttpErrorResponse) {
      const status = error.status;

      if (status === 0) {
        return `${context}: Unable to reach the server. Please check if the backend is running.`;
      }

      if (status >= 400 && status < 500) {
        const backendMessage = (error.error && (error.error.message || error.error.detail)) ?? null;
        if (backendMessage) {
          return `${context}: ${backendMessage}`;
        }
        return `${context}: The request seems invalid (error ${status}). Please check your input and try again.`;
      }

      if (status >= 500) {
        return `${context}: A server error occurred (error ${status}). Please try again later.`;
      }
    }

    return `${context}: An unexpected error occurred. Please try again.`;
  }

  searchShipment() {
    this.errorMessage = null;
    const shipmentId = this.searchForm.value.shipmentId;
    this.service.getShipment(shipmentId).subscribe({
      next: data => {
        this.shipmentData = data;
      },
      error: err => {
        this.errorMessage = this.buildErrorMessage('Failed to load shipment', err);
        console.error('Search shipment error', err);
      }
    });
  }

  calculateProfit() {
    this.errorMessage = null;
    const shipmentId = this.searchForm.value.shipmentId?.toString().trim();
    const calculation = this.calculationForm.value;

    // If a shipment is selected, add cargo to that shipment
    if (shipmentId) {
      this.service.createCargo(+shipmentId, calculation).subscribe({
        next: () => {
          this.refreshCargos(shipmentId);
          this.calculationForm.reset();
        },
        error: err => {
          this.errorMessage = this.buildErrorMessage('Failed to calculate profit', err);
          console.error('Calculate profit error', err);
        }
      });
      return;
    }

    // If no shipment is selected, create a new shipment with this cargo
    this.service.createShipment(calculation).subscribe({
      next: created => {
        const newShipmentId = created.shipmentId;
        if (newShipmentId) {
          this.searchForm.patchValue({shipmentId: newShipmentId.toString()});
          this.refreshCargos(newShipmentId);
        }
        this.calculationForm.reset();
      },
      error: err => {
        this.errorMessage = this.buildErrorMessage('Failed to create shipment', err);
        console.error('Create shipment error', err);
      }
    });
  }

  deleteCargo(cargoId: number) {
    this.errorMessage = null;
    const shipmentId = this.searchForm.value.shipmentId;
    if (!shipmentId) {
      return;
    }
    this.service.deleteCargo(cargoId).subscribe({
      next: () => {
        this.refreshCargos(shipmentId);
      },
      error: err => {
        this.errorMessage = this.buildErrorMessage('Failed to delete cargo', err);
        console.error('Delete cargo error', err);
      }
    });
  }

  private refreshCargos(shipmentId: string | number) {
    this.service.getShipmentWithCargos(+shipmentId).subscribe(data => {
      this.shipmentData = data;
    });
  }
}
