import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPointUseDet, PointUseDet } from '../point-use-det.model';
import { PointUseDetService } from '../service/point-use-det.service';

@Injectable({ providedIn: 'root' })
export class PointUseDetRoutingResolveService implements Resolve<IPointUseDet> {
  constructor(protected service: PointUseDetService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPointUseDet> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((pointUseDet: HttpResponse<PointUseDet>) => {
          if (pointUseDet.body) {
            return of(pointUseDet.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PointUseDet());
  }
}
