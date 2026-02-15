import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({providedIn: 'root'})
export class CalculateProfitService {
  constructor(private http: HttpClient) {
  }

  getShipment(shipmentId: string): Observable<any> {
    return this.http.get(`/api/v1/shipments/${shipmentId}`);
  }

  createCargo(shipmentId: number, calculation: {
    income: number;
    cost: number;
    additionalCost: number
  }): Observable<any> {
    return this.http.post(`/api/v1/shipments/${shipmentId}/cargos`, calculation);
  }

  createShipment(calculation: { income: number; cost: number; additionalCost: number }): Observable<any> {
    return this.http.post(`/api/v1/shipments`, calculation);
  }

  getShipmentWithCargos(id: number): Observable<any> {
    return this.http.get(`/api/v1/shipments/${id}`);
  }

  deleteCargo(cargoId: number): Observable<void> {
    return this.http.delete<void>(`/api/v1/shipments/cargos/${cargoId}`);
  }
}
