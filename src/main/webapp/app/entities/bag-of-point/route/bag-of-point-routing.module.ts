import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BagOfPointComponent } from '../list/bag-of-point.component';
import { BagOfPointDetailComponent } from '../detail/bag-of-point-detail.component';
import { BagOfPointUpdateComponent } from '../update/bag-of-point-update.component';
import { BagOfPointRoutingResolveService } from './bag-of-point-routing-resolve.service';

const bagOfPointRoute: Routes = [
  {
    path: '',
    component: BagOfPointComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BagOfPointDetailComponent,
    resolve: {
      bagOfPoint: BagOfPointRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BagOfPointUpdateComponent,
    resolve: {
      bagOfPoint: BagOfPointRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BagOfPointUpdateComponent,
    resolve: {
      bagOfPoint: BagOfPointRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(bagOfPointRoute)],
  exports: [RouterModule],
})
export class BagOfPointRoutingModule {}
