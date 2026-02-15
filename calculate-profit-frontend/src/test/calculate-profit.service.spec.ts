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
    service.getShipment('SHIP123').subscribe(data => {
      expect(data).toEqual({id: 'SHIP123'});
    });
    const req = httpMock.expectOne('/api/v1/shipments/SHIP123');
    expect(req.request.method).toBe('GET');
    req.flush({id: 'SHIP123'});
  });

  it('should call createCargo', () => {
    const calculation = {income: 100, cost: 50, additionalCost: 10};
    service.createCargo(123, calculation).subscribe(data => {
      expect(data).toEqual({income: 100, totalCost: 60, profitOrLoss: 40});
    });
    const req = httpMock.expectOne('/api/v1/shipments/123/cargos');
    expect(req.request.method).toBe('POST');
    req.flush({income: 100, totalCost: 60, profitOrLoss: 40});
  });

  it('should call createShipment', () => {
    const calculation = {income: 100, cost: 50, additionalCost: 10};
    service.createShipment(calculation).subscribe(data => {
      expect(data).toEqual({income: 100, totalCost: 60, profitOrLoss: 40});
    });
    const req = httpMock.expectOne('/api/v1/shipments');
    expect(req.request.method).toBe('POST');
    req.flush({income: 100, totalCost: 60, profitOrLoss: 40});
  });

  it('should call getShipmentWithCargos', () => {
    service.getShipmentWithCargos(123).subscribe(data => {
      expect(data).toEqual({id: 123, cargos: [{income: 100, cost: 50, additionalCost: 10}]});
    });
    const req = httpMock.expectOne('/api/v1/shipments/123');
    expect(req.request.method).toBe('GET');
    req.flush({id: 123, cargos: [{income: 100, cost: 50, additionalCost: 10}]});
  });
});
