export interface CalculationRequest {
  income: number;
  cost: number;
  additionalCost: number;
}

export interface Cargo {
  id: number;
  income: number;
  totalCost: number;
  profit: number;
  shipmentId: number;
}

export interface Shipment {
  shipmentId: number;
  cargos: Cargo[];
}
