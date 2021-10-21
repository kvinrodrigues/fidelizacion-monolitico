import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IExpirationPoint } from '../expiration-point.model';
import { ExpirationPointService } from '../service/expiration-point.service';

@Component({
  templateUrl: './expiration-point-delete-dialog.component.html',
})
export class ExpirationPointDeleteDialogComponent {
  expirationPoint?: IExpirationPoint;

  constructor(protected expirationPointService: ExpirationPointService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.expirationPointService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
