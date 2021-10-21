import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PointUsageConceptComponent } from '../list/point-usage-concept.component';
import { PointUsageConceptDetailComponent } from '../detail/point-usage-concept-detail.component';
import { PointUsageConceptUpdateComponent } from '../update/point-usage-concept-update.component';
import { PointUsageConceptRoutingResolveService } from './point-usage-concept-routing-resolve.service';

const pointUsageConceptRoute: Routes = [
  {
    path: '',
    component: PointUsageConceptComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PointUsageConceptDetailComponent,
    resolve: {
      pointUsageConcept: PointUsageConceptRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PointUsageConceptUpdateComponent,
    resolve: {
      pointUsageConcept: PointUsageConceptRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PointUsageConceptUpdateComponent,
    resolve: {
      pointUsageConcept: PointUsageConceptRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(pointUsageConceptRoute)],
  exports: [RouterModule],
})
export class PointUsageConceptRoutingModule {}
