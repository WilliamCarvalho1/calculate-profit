import {TestBed} from '@angular/core/testing';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {CalculateProfitService} from '../app/calculate-profit/calculate-profit.service';

describe('CalculateProfitService', () => {
  let service: CalculateProfitService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [CalculateProfitService]
    });
    service = TestBed.inject(CalculateProfitService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should call getShipment', () => {
    service.getShipment(123).subscribe(data => {
      expect(data).toEqual({shipmentId: 123, cargos: []});
    });
    const req = httpMock.expectOne('/api/v1/shipments/123');
    expect(req.request.method).toBe('GET');
    req.flush({shipmentId: 123, cargos: []});
  });

  it('should call createCargo', () => {
    const calculation = {income: 100, cost: 50, additionalCost: 10};
    service.createCargo(123, calculation).subscribe(data => {
      expect(data).toEqual({id: 1, income: 100, totalCost: 60, profit: 40, shipmentId: 123});
    });
    const req = httpMock.expectOne('/api/v1/shipments/123/cargos');
    expect(req.request.method).toBe('POST');
    req.flush({id: 1, income: 100, totalCost: 60, profit: 40, shipmentId: 123});
  });

  it('should call createShipment', () => {
    const calculation = {income: 100, cost: 50, additionalCost: 10};
    service.createShipment(calculation).subscribe(data => {
      expect(data).toEqual({id: 1, income: 100, totalCost: 60, profit: 40, shipmentId: 123});
    });
    const req = httpMock.expectOne('/api/v1/shipments');
    expect(req.request.method).toBe('POST');
    req.flush({id: 1, income: 100, totalCost: 60, profit: 40, shipmentId: 123});
  });

  it('should call getShipmentWithCargos', () => {
    service.getShipment(123).subscribe(data => {
      expect(data).toEqual({shipmentId: 123, cargos: [{id: 1, income: 100, totalCost: 60, profit: 40, shipmentId: 123}]});
    });
    const req = httpMock.expectOne('/api/v1/shipments/123');
    expect(req.request.method).toBe('GET');
    req.flush({shipmentId: 123, cargos: [{id: 1, income: 100, totalCost: 60, profit: 40, shipmentId: 123}]});
  });
});
