import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ExpirationPointComponent } from './list/expiration-point.component';
import { ExpirationPointDetailComponent } from './detail/expiration-point-detail.component';
import { ExpirationPointUpdateComponent } from './update/expiration-point-update.component';
import { ExpirationPointDeleteDialogComponent } from './delete/expiration-point-delete-dialog.component';
import { ExpirationPointRoutingModule } from './route/expiration-point-routing.module';

@NgModule({
  imports: [SharedModule, ExpirationPointRoutingModule],
  declarations: [
    ExpirationPointComponent,
    ExpirationPointDetailComponent,
    ExpirationPointUpdateComponent,
    ExpirationPointDeleteDialogComponent,
  ],
  entryComponents: [ExpirationPointDeleteDialogComponent],
})
export class ExpirationPointModule {}
