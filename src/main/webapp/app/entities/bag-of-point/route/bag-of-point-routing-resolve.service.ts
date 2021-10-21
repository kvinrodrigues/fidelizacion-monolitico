import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBagOfPoint, BagOfPoint } from '../bag-of-point.model';
import { BagOfPointService } from '../service/bag-of-point.service';

@Injectable({ providedIn: 'root' })
export class BagOfPointRoutingResolveService implements Resolve<IBagOfPoint> {
  constructor(protected service: BagOfPointService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBagOfPoint> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((bagOfPoint: HttpResponse<BagOfPoint>) => {
          if (bagOfPoint.body) {
            return of(bagOfPoint.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new BagOfPoint());
  }
}
