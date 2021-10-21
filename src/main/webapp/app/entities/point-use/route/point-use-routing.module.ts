import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PointUseComponent } from '../list/point-use.component';
import { PointUseDetailComponent } from '../detail/point-use-detail.component';
import { PointUseUpdateComponent } from '../update/point-use-update.component';
import { PointUseRoutingResolveService } from './point-use-routing-resolve.service';

const pointUseRoute: Routes = [
  {
    path: '',
    component: PointUseComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PointUseDetailComponent,
    resolve: {
      pointUse: PointUseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PointUseUpdateComponent,
    resolve: {
      pointUse: PointUseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PointUseUpdateComponent,
    resolve: {
      pointUse: PointUseRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(pointUseRoute)],
  exports: [RouterModule],
})
export class PointUseRoutingModule {}
