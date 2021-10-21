import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IExpirationPoint, ExpirationPoint } from '../expiration-point.model';
import { ExpirationPointService } from '../service/expiration-point.service';

@Injectable({ providedIn: 'root' })
export class ExpirationPointRoutingResolveService implements Resolve<IExpirationPoint> {
  constructor(protected service: ExpirationPointService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IExpirationPoint> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((expirationPoint: HttpResponse<ExpirationPoint>) => {
          if (expirationPoint.body) {
            return of(expirationPoint.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ExpirationPoint());
  }
}
