jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPointUseDet, PointUseDet } from '../point-use-det.model';
import { PointUseDetService } from '../service/point-use-det.service';

import { PointUseDetRoutingResolveService } from './point-use-det-routing-resolve.service';

describe('PointUseDet routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: PointUseDetRoutingResolveService;
  let service: PointUseDetService;
  let resultPointUseDet: IPointUseDet | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(PointUseDetRoutingResolveService);
    service = TestBed.inject(PointUseDetService);
    resultPointUseDet = undefined;
  });

  describe('resolve', () => {
    it('should return IPointUseDet returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPointUseDet = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPointUseDet).toEqual({ id: 123 });
    });

    it('should return new IPointUseDet if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPointUseDet = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPointUseDet).toEqual(new PointUseDet());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as PointUseDet })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPointUseDet = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPointUseDet).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
