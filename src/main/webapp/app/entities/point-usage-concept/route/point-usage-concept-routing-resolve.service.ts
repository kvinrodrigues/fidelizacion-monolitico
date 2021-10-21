import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPointUsageConcept, PointUsageConcept } from '../point-usage-concept.model';
import { PointUsageConceptService } from '../service/point-usage-concept.service';

@Injectable({ providedIn: 'root' })
export class PointUsageConceptRoutingResolveService implements Resolve<IPointUsageConcept> {
  constructor(protected service: PointUsageConceptService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPointUsageConcept> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((pointUsageConcept: HttpResponse<PointUsageConcept>) => {
          if (pointUsageConcept.body) {
            return of(pointUsageConcept.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PointUsageConcept());
  }
}
