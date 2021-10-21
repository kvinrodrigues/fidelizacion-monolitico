import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PointUseComponent } from './list/point-use.component';
import { PointUseDetailComponent } from './detail/point-use-detail.component';
import { PointUseUpdateComponent } from './update/point-use-update.component';
import { PointUseDeleteDialogComponent } from './delete/point-use-delete-dialog.component';
import { PointUseRoutingModule } from './route/point-use-routing.module';

@NgModule({
  imports: [SharedModule, PointUseRoutingModule],
  declarations: [PointUseComponent, PointUseDetailComponent, PointUseUpdateComponent, PointUseDeleteDialogComponent],
  entryComponents: [PointUseDeleteDialogComponent],
})
export class PointUseModule {}
