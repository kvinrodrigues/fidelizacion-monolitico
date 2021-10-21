import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPointUse, PointUse } from '../point-use.model';
import { PointUseService } from '../service/point-use.service';

@Injectable({ providedIn: 'root' })
export class PointUseRoutingResolveService implements Resolve<IPointUse> {
  constructor(protected service: PointUseService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPointUse> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((pointUse: HttpResponse<PointUse>) => {
          if (pointUse.body) {
            return of(pointUse.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PointUse());
  }
}
