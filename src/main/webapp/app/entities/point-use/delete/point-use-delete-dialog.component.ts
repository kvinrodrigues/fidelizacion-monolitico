import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPointUse } from '../point-use.model';
import { PointUseService } from '../service/point-use.service';

@Component({
  templateUrl: './point-use-delete-dialog.component.html',
})
export class PointUseDeleteDialogComponent {
  pointUse?: IPointUse;

  constructor(protected pointUseService: PointUseService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pointUseService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
