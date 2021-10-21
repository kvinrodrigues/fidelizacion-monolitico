import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { NacionalityComponent } from '../list/nacionality.component';
import { NacionalityDetailComponent } from '../detail/nacionality-detail.component';
import { NacionalityUpdateComponent } from '../update/nacionality-update.component';
import { NacionalityRoutingResolveService } from './nacionality-routing-resolve.service';

const nacionalityRoute: Routes = [
  {
    path: '',
    component: NacionalityComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: NacionalityDetailComponent,
    resolve: {
      nacionality: NacionalityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: NacionalityUpdateComponent,
    resolve: {
      nacionality: NacionalityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: NacionalityUpdateComponent,
    resolve: {
      nacionality: NacionalityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(nacionalityRoute)],
  exports: [RouterModule],
})
export class NacionalityRoutingModule {}
