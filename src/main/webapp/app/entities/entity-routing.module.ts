import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'document-type',
        data: { pageTitle: 'DocumentTypes' },
        loadChildren: () => import('./document-type/document-type.module').then(m => m.DocumentTypeModule),
      },
      {
        path: 'nacionality',
        data: { pageTitle: 'Nacionalities' },
        loadChildren: () => import('./nacionality/nacionality.module').then(m => m.NacionalityModule),
      },
      {
        path: 'client',
        data: { pageTitle: 'Clients' },
        loadChildren: () => import('./client/client.module').then(m => m.ClientModule),
      },
      {
        path: 'point-usage-concept',
        data: { pageTitle: 'PointUsageConcepts' },
        loadChildren: () => import('./point-usage-concept/point-usage-concept.module').then(m => m.PointUsageConceptModule),
      },
      {
        path: 'point-allocation-rule',
        data: { pageTitle: 'PointAllocationRules' },
        loadChildren: () => import('./point-allocation-rule/point-allocation-rule.module').then(m => m.PointAllocationRuleModule),
      },
      {
        path: 'expiration-point',
        data: { pageTitle: 'ExpirationPoints' },
        loadChildren: () => import('./expiration-point/expiration-point.module').then(m => m.ExpirationPointModule),
      },
      {
        path: 'bag-of-point',
        data: { pageTitle: 'BagOfPoints' },
        loadChildren: () => import('./bag-of-point/bag-of-point.module').then(m => m.BagOfPointModule),
      },
      {
        path: 'point-use',
        data: { pageTitle: 'PointUses' },
        loadChildren: () => import('./point-use/point-use.module').then(m => m.PointUseModule),
      },
      {
        path: 'point-use-det',
        data: { pageTitle: 'PointUseDets' },
        loadChildren: () => import('./point-use-det/point-use-det.module').then(m => m.PointUseDetModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
