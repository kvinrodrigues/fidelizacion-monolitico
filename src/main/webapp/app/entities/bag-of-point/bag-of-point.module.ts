import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BagOfPointComponent } from './list/bag-of-point.component';
import { BagOfPointDetailComponent } from './detail/bag-of-point-detail.component';
import { BagOfPointUpdateComponent } from './update/bag-of-point-update.component';
import { BagOfPointDeleteDialogComponent } from './delete/bag-of-point-delete-dialog.component';
import { BagOfPointRoutingModule } from './route/bag-of-point-routing.module';

@NgModule({
  imports: [SharedModule, BagOfPointRoutingModule],
  declarations: [BagOfPointComponent, BagOfPointDetailComponent, BagOfPointUpdateComponent, BagOfPointDeleteDialogComponent],
  entryComponents: [BagOfPointDeleteDialogComponent],
})
export class BagOfPointModule {}
