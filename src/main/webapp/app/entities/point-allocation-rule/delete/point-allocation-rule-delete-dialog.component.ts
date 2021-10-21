import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPointAllocationRule } from '../point-allocation-rule.model';
import { PointAllocationRuleService } from '../service/point-allocation-rule.service';

@Component({
  templateUrl: './point-allocation-rule-delete-dialog.component.html',
})
export class PointAllocationRuleDeleteDialogComponent {
  pointAllocationRule?: IPointAllocationRule;

  constructor(protected pointAllocationRuleService: PointAllocationRuleService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pointAllocationRuleService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
