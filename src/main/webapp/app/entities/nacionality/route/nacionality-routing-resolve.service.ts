import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { INacionality, Nacionality } from '../nacionality.model';
import { NacionalityService } from '../service/nacionality.service';

@Injectable({ providedIn: 'root' })
export class NacionalityRoutingResolveService implements Resolve<INacionality> {
  constructor(protected service: NacionalityService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<INacionality> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((nacionality: HttpResponse<Nacionality>) => {
          if (nacionality.body) {
            return of(nacionality.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Nacionality());
  }
}
