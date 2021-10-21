import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { NacionalityComponent } from './list/nacionality.component';
import { NacionalityDetailComponent } from './detail/nacionality-detail.component';
import { NacionalityUpdateComponent } from './update/nacionality-update.component';
import { NacionalityDeleteDialogComponent } from './delete/nacionality-delete-dialog.component';
import { NacionalityRoutingModule } from './route/nacionality-routing.module';

@NgModule({
  imports: [SharedModule, NacionalityRoutingModule],
  declarations: [NacionalityComponent, NacionalityDetailComponent, NacionalityUpdateComponent, NacionalityDeleteDialogComponent],
  entryComponents: [NacionalityDeleteDialogComponent],
})
export class NacionalityModule {}
