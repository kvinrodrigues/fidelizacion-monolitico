jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { INacionality, Nacionality } from '../nacionality.model';
import { NacionalityService } from '../service/nacionality.service';

import { NacionalityRoutingResolveService } from './nacionality-routing-resolve.service';

describe('Nacionality routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: NacionalityRoutingResolveService;
  let service: NacionalityService;
  let resultNacionality: INacionality | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(NacionalityRoutingResolveService);
    service = TestBed.inject(NacionalityService);
    resultNacionality = undefined;
  });

  describe('resolve', () => {
    it('should return INacionality returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultNacionality = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultNacionality).toEqual({ id: 123 });
    });

    it('should return new INacionality if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultNacionality = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultNacionality).toEqual(new Nacionality());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Nacionality })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultNacionality = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultNacionality).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
