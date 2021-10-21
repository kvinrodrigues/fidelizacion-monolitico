jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPointUsageConcept, PointUsageConcept } from '../point-usage-concept.model';
import { PointUsageConceptService } from '../service/point-usage-concept.service';

import { PointUsageConceptRoutingResolveService } from './point-usage-concept-routing-resolve.service';

describe('PointUsageConcept routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: PointUsageConceptRoutingResolveService;
  let service: PointUsageConceptService;
  let resultPointUsageConcept: IPointUsageConcept | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(PointUsageConceptRoutingResolveService);
    service = TestBed.inject(PointUsageConceptService);
    resultPointUsageConcept = undefined;
  });

  describe('resolve', () => {
    it('should return IPointUsageConcept returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPointUsageConcept = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPointUsageConcept).toEqual({ id: 123 });
    });

    it('should return new IPointUsageConcept if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPointUsageConcept = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultPointUsageConcept).toEqual(new PointUsageConcept());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as PointUsageConcept })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultPointUsageConcept = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultPointUsageConcept).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
