jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPointUse, PointUse } from '../point-use.model';
import { PointUseService } from '../service/point-use.service';

import { PointUseRoutingResolveService } from './point-use-routing-resolve.service';

describe('PointUse routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: PointUseRoutingResolveService;
  let service: PointUseService;
  let resultPointUse: IPointUse | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(PointUseRoutingResolveService);
    service = TestBed.inject(PointUseService);
    resultPointUse = undefined;
  });

  describe('resolve', () => {
    it('should return IPointUse returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPointUse = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPointUse).toEqual({ id: 123 });
    });

    it('should return new IPointUse if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPointUse = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPointUse).toEqual(new PointUse());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as PointUse })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPointUse = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPointUse).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
