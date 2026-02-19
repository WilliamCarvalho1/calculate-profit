import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {CalculationRequest, Cargo, Shipment} from './calculation.model';

@Injectable({providedIn: 'root'})
export class CalculateProfitService {
  constructor(private http: HttpClient) {
  }

  getShipment(shipmentId: number): Observable<Shipment> {
    return this.http.get<Shipment>(`/api/v1/shipments/${shipmentId}`);
  }

  createCargo(shipmentId: number, calculation: CalculationRequest): Observable<Cargo> {
    return this.http.post<Cargo>(`/api/v1/shipments/${shipmentId}/cargos`, calculation);
  }

  createShipment(calculation: CalculationRequest): Observable<Cargo> {
    return this.http.post<Cargo>(`/api/v1/shipments`, calculation);
  }

  deleteCargo(cargoId: number): Observable<void> {
    return this.http.delete<void>(`/api/v1/shipments/cargos/${cargoId}`);
  }
}
