import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBagOfPoint } from '../bag-of-point.model';
import { BagOfPointService } from '../service/bag-of-point.service';

@Component({
  templateUrl: './bag-of-point-delete-dialog.component.html',
})
export class BagOfPointDeleteDialogComponent {
  bagOfPoint?: IBagOfPoint;

  constructor(protected bagOfPointService: BagOfPointService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bagOfPointService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
