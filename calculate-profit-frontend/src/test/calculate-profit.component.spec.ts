import {CalculateProfitComponent} from '../app/calculate-profit/calculate-profit.component';
import {FormBuilder} from '@angular/forms';
import {of, Subject} from 'rxjs';
import {MatDialog} from '@angular/material/dialog';

class MockService {
  getShipment = jasmine.createSpy().and.returnValues(
    of({shipmentId: 123, cargos: []}),
    of({shipmentId: 123, cargos: [{id: 1, income: 100, totalCost: 60, profit: 40, shipmentId: 123}]})
  );
  createCargo = jasmine
    .createSpy()
    .and.returnValue(of({id: 1, income: 100, totalCost: 60, profit: 40, shipmentId: 123}));
  deleteCargo = jasmine.createSpy().and.returnValue(of(void 0));
}

class MockDialog {
  // Will be set per test to control dialog outcome
  private closeSubject = new Subject<boolean>();

  open(): any {
    return {
      afterClosed: () => this.closeSubject.asObservable()
    };
  }

  closeWith(value: boolean) {
    this.closeSubject.next(value);
    this.closeSubject.complete();
    this.closeSubject = new Subject<boolean>();
  }
}

describe('CalculateProfitComponent', () => {
  let component: CalculateProfitComponent;
  let service: MockService;
  let dialog: MockDialog;

  beforeEach(() => {
    service = new MockService();
    dialog = new MockDialog();
    component = new CalculateProfitComponent(new FormBuilder(), service as any, dialog as unknown as MatDialog);
  });

  it('should create forms', () => {
    expect(component.searchForm).toBeDefined();
    expect(component.calculationForm).toBeDefined();
  });

  it('should call getShipment and set shipmentData', () => {
    component.searchForm.setValue({shipmentId: '123'});
    component.searchShipment();
    expect(service.getShipment).toHaveBeenCalledWith(123);
    expect(component.shipmentData).toEqual({shipmentId: 123, cargos: []});
  });

  it('should call createCargo and refresh cargos table', () => {
    // First perform an initial search to populate shipmentData via the first getShipment call
    component.searchForm.setValue({shipmentId: '123'});
    component.searchShipment();

    // Now perform the calculation which should create a cargo and then refresh cargos via a second getShipment call
    component.calculationForm.setValue({income: 100, cost: 50, additionalCost: 10});
    component.calculateProfit();

    expect(service.createCargo).toHaveBeenCalledWith(123, {income: 100, cost: 50, additionalCost: 10});
    expect(service.getShipment).toHaveBeenCalledWith(123);
    expect(component.shipmentData!.cargos!.length).toBe(1);
    expect(component.shipmentData!.cargos![0]).toEqual({id: 1, income: 100, totalCost: 60, profit: 40, shipmentId: 123});
  });

  it('should call deleteCargo when confirmation dialog is confirmed', () => {
    component.searchForm.setValue({shipmentId: '123'});

    // Spy on component.deleteCargo to ensure it is invoked
    const deleteSpy = spyOn(component, 'deleteCargo');

    component.confirmDeleteCargo(5);

    dialog.closeWith(true);

    expect(deleteSpy).toHaveBeenCalledWith(5);
  });

  it('should NOT call deleteCargo when confirmation dialog is cancelled', () => {
    component.searchForm.setValue({shipmentId: '123'});

    const deleteSpy = spyOn(component, 'deleteCargo');

    component.confirmDeleteCargo(5);

    dialog.closeWith(false);

    expect(deleteSpy).not.toHaveBeenCalled();
  });
});
