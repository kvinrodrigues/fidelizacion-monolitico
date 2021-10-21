import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPointUsageConcept } from '../point-usage-concept.model';
import { PointUsageConceptService } from '../service/point-usage-concept.service';

@Component({
  templateUrl: './point-usage-concept-delete-dialog.component.html',
})
export class PointUsageConceptDeleteDialogComponent {
  pointUsageConcept?: IPointUsageConcept;

  constructor(protected pointUsageConceptService: PointUsageConceptService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pointUsageConceptService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
