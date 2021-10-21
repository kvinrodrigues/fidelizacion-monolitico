import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PointUseDetComponent } from '../list/point-use-det.component';
import { PointUseDetDetailComponent } from '../detail/point-use-det-detail.component';
import { PointUseDetUpdateComponent } from '../update/point-use-det-update.component';
import { PointUseDetRoutingResolveService } from './point-use-det-routing-resolve.service';

const pointUseDetRoute: Routes = [
  {
    path: '',
    component: PointUseDetComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PointUseDetDetailComponent,
    resolve: {
      pointUseDet: PointUseDetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PointUseDetUpdateComponent,
    resolve: {
      pointUseDet: PointUseDetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PointUseDetUpdateComponent,
    resolve: {
      pointUseDet: PointUseDetRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(pointUseDetRoute)],
  exports: [RouterModule],
})
export class PointUseDetRoutingModule {}
