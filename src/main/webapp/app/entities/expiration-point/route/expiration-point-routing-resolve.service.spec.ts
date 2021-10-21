jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IExpirationPoint, ExpirationPoint } from '../expiration-point.model';
import { ExpirationPointService } from '../service/expiration-point.service';

import { ExpirationPointRoutingResolveService } from './expiration-point-routing-resolve.service';

describe('ExpirationPoint routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ExpirationPointRoutingResolveService;
  let service: ExpirationPointService;
  let resultExpirationPoint: IExpirationPoint | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(ExpirationPointRoutingResolveService);
    service = TestBed.inject(ExpirationPointService);
    resultExpirationPoint = undefined;
  });

  describe('resolve', () => {
    it('should return IExpirationPoint returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultExpirationPoint = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultExpirationPoint).toEqual({ id: 123 });
    });

    it('should return new IExpirationPoint if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultExpirationPoint = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultExpirationPoint).toEqual(new ExpirationPoint());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ExpirationPoint })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultExpirationPoint = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultExpirationPoint).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
