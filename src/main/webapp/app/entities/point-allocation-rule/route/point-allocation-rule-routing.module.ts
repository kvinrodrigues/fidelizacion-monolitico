import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PointAllocationRuleComponent } from '../list/point-allocation-rule.component';
import { PointAllocationRuleDetailComponent } from '../detail/point-allocation-rule-detail.component';
import { PointAllocationRuleUpdateComponent } from '../update/point-allocation-rule-update.component';
import { PointAllocationRuleRoutingResolveService } from './point-allocation-rule-routing-resolve.service';

const pointAllocationRuleRoute: Routes = [
  {
    path: '',
    component: PointAllocationRuleComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PointAllocationRuleDetailComponent,
    resolve: {
      pointAllocationRule: PointAllocationRuleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PointAllocationRuleUpdateComponent,
    resolve: {
      pointAllocationRule: PointAllocationRuleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PointAllocationRuleUpdateComponent,
    resolve: {
      pointAllocationRule: PointAllocationRuleRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(pointAllocationRuleRoute)],
  exports: [RouterModule],
})
export class PointAllocationRuleRoutingModule {}
