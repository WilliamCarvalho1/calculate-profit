import {CalculateProfitComponent} from '../app/calculate-profit/calculate-profit.component';
import {FormBuilder} from '@angular/forms';
import {of} from 'rxjs';

class MockService {
  getShipment = jasmine.createSpy().and.returnValue(of({shipmentId: 123, cargos: []}));
  createCargo = jasmine.createSpy().and.returnValue(of({income: 100, totalCost: 60, profitOrLoss: 40}));
  getShipmentWithCargos = jasmine.createSpy().and.returnValue(of({
    shipmentId: 123,
    cargos: [{income: 100, totalCost: 60, profitOrLoss: 40}]
  }));
}

describe('CalculateProfitComponent', () => {
  let component: CalculateProfitComponent;
  let service: MockService;

  beforeEach(() => {
    service = new MockService();
    component = new CalculateProfitComponent(new FormBuilder(), service as any);
  });

  it('should create forms', () => {
    expect(component.searchForm).toBeDefined();
    expect(component.calculationForm).toBeDefined();
  });

  it('should call getShipment and set shipmentData', () => {
    component.searchForm.setValue({shipmentId: '123'});
    component.searchShipment();
    expect(service.getShipment).toHaveBeenCalledWith('123');
    expect(component.shipmentData).toEqual({shipmentId: 123, cargos: []});
  });

  it('should call createCargo and refresh cargos table', () => {
    component.searchForm.setValue({shipmentId: '123'});
    component.calculationForm.setValue({income: 100, cost: 50, additionalCost: 10});
    component.calculateProfit();
    expect(service.createCargo).toHaveBeenCalledWith('123', {income: 100, cost: 50, additionalCost: 10});
    expect(service.getShipmentWithCargos).toHaveBeenCalledWith('123');
    expect(component.shipmentData.cargos.length).toBe(1);
    expect(component.shipmentData.cargos[0]).toEqual({income: 100, totalCost: 60, profitOrLoss: 40});
  });
});
