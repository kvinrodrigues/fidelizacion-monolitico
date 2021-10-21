jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPointAllocationRule, PointAllocationRule } from '../point-allocation-rule.model';
import { PointAllocationRuleService } from '../service/point-allocation-rule.service';

import { PointAllocationRuleRoutingResolveService } from './point-allocation-rule-routing-resolve.service';

describe('PointAllocationRule routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: PointAllocationRuleRoutingResolveService;
  let service: PointAllocationRuleService;
  let resultPointAllocationRule: IPointAllocationRule | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(PointAllocationRuleRoutingResolveService);
    service = TestBed.inject(PointAllocationRuleService);
    resultPointAllocationRule = undefined;
  });

  describe('resolve', () => {
    it('should return IPointAllocationRule returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPointAllocationRule = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPointAllocationRule).toEqual({ id: 123 });
    });

    it('should return new IPointAllocationRule if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPointAllocationRule = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPointAllocationRule).toEqual(new PointAllocationRule());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as PointAllocationRule })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPointAllocationRule = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPointAllocationRule).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
