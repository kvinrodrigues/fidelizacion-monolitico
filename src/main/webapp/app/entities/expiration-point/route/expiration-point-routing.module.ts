import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ExpirationPointComponent } from '../list/expiration-point.component';
import { ExpirationPointDetailComponent } from '../detail/expiration-point-detail.component';
import { ExpirationPointUpdateComponent } from '../update/expiration-point-update.component';
import { ExpirationPointRoutingResolveService } from './expiration-point-routing-resolve.service';

const expirationPointRoute: Routes = [
  {
    path: '',
    component: ExpirationPointComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ExpirationPointDetailComponent,
    resolve: {
      expirationPoint: ExpirationPointRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ExpirationPointUpdateComponent,
    resolve: {
      expirationPoint: ExpirationPointRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ExpirationPointUpdateComponent,
    resolve: {
      expirationPoint: ExpirationPointRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(expirationPointRoute)],
  exports: [RouterModule],
})
export class ExpirationPointRoutingModule {}
