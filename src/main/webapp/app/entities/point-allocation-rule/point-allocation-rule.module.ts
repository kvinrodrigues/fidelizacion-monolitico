import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PointAllocationRuleComponent } from './list/point-allocation-rule.component';
import { PointAllocationRuleDetailComponent } from './detail/point-allocation-rule-detail.component';
import { PointAllocationRuleUpdateComponent } from './update/point-allocation-rule-update.component';
import { PointAllocationRuleDeleteDialogComponent } from './delete/point-allocation-rule-delete-dialog.component';
import { PointAllocationRuleRoutingModule } from './route/point-allocation-rule-routing.module';

@NgModule({
  imports: [SharedModule, PointAllocationRuleRoutingModule],
  declarations: [
    PointAllocationRuleComponent,
    PointAllocationRuleDetailComponent,
    PointAllocationRuleUpdateComponent,
    PointAllocationRuleDeleteDialogComponent,
  ],
  entryComponents: [PointAllocationRuleDeleteDialogComponent],
})
export class PointAllocationRuleModule {}
