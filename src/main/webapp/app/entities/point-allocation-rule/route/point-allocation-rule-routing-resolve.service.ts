import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPointAllocationRule, PointAllocationRule } from '../point-allocation-rule.model';
import { PointAllocationRuleService } from '../service/point-allocation-rule.service';

@Injectable({ providedIn: 'root' })
export class PointAllocationRuleRoutingResolveService implements Resolve<IPointAllocationRule> {
  constructor(protected service: PointAllocationRuleService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPointAllocationRule> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((pointAllocationRule: HttpResponse<PointAllocationRule>) => {
          if (pointAllocationRule.body) {
            return of(pointAllocationRule.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PointAllocationRule());
  }
}
