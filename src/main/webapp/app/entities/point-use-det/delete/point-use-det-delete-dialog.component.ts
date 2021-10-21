import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPointUseDet } from '../point-use-det.model';
import { PointUseDetService } from '../service/point-use-det.service';

@Component({
  templateUrl: './point-use-det-delete-dialog.component.html',
})
export class PointUseDetDeleteDialogComponent {
  pointUseDet?: IPointUseDet;

  constructor(protected pointUseDetService: PointUseDetService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pointUseDetService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
