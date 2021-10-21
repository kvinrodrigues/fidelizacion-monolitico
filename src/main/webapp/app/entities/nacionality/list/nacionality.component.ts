import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { INacionality } from '../nacionality.model';
import { NacionalityService } from '../service/nacionality.service';
import { NacionalityDeleteDialogComponent } from '../delete/nacionality-delete-dialog.component';

@Component({
  selector: 'jhi-nacionality',
  templateUrl: './nacionality.component.html',
})
export class NacionalityComponent implements OnInit {
  nacionalities?: INacionality[];
  isLoading = false;

  constructor(protected nacionalityService: NacionalityService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.nacionalityService.query().subscribe(
      (res: HttpResponse<INacionality[]>) => {
        this.isLoading = false;
        this.nacionalities = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: INacionality): number {
    return item.id!;
  }

  delete(nacionality: INacionality): void {
    const modalRef = this.modalService.open(NacionalityDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.nacionality = nacionality;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
