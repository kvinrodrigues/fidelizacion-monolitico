import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PointUsageConceptComponent } from './list/point-usage-concept.component';
import { PointUsageConceptDetailComponent } from './detail/point-usage-concept-detail.component';
import { PointUsageConceptUpdateComponent } from './update/point-usage-concept-update.component';
import { PointUsageConceptDeleteDialogComponent } from './delete/point-usage-concept-delete-dialog.component';
import { PointUsageConceptRoutingModule } from './route/point-usage-concept-routing.module';

@NgModule({
  imports: [SharedModule, PointUsageConceptRoutingModule],
  declarations: [
    PointUsageConceptComponent,
    PointUsageConceptDetailComponent,
    PointUsageConceptUpdateComponent,
    PointUsageConceptDeleteDialogComponent,
  ],
  entryComponents: [PointUsageConceptDeleteDialogComponent],
})
export class PointUsageConceptModule {}
