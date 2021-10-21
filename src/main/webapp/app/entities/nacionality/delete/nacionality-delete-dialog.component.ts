import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { INacionality } from '../nacionality.model';
import { NacionalityService } from '../service/nacionality.service';

@Component({
  templateUrl: './nacionality-delete-dialog.component.html',
})
export class NacionalityDeleteDialogComponent {
  nacionality?: INacionality;

  constructor(protected nacionalityService: NacionalityService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.nacionalityService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
