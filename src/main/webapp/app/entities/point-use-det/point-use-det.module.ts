import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PointUseDetComponent } from './list/point-use-det.component';
import { PointUseDetDetailComponent } from './detail/point-use-det-detail.component';
import { PointUseDetUpdateComponent } from './update/point-use-det-update.component';
import { PointUseDetDeleteDialogComponent } from './delete/point-use-det-delete-dialog.component';
import { PointUseDetRoutingModule } from './route/point-use-det-routing.module';

@NgModule({
  imports: [SharedModule, PointUseDetRoutingModule],
  declarations: [PointUseDetComponent, PointUseDetDetailComponent, PointUseDetUpdateComponent, PointUseDetDeleteDialogComponent],
  entryComponents: [PointUseDetDeleteDialogComponent],
})
export class PointUseDetModule {}
