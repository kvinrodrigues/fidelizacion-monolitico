import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IExpirationPoint } from '../expiration-point.model';
import { ExpirationPointService } from '../service/expiration-point.service';
import { ExpirationPointDeleteDialogComponent } from '../delete/expiration-point-delete-dialog.component';

@Component({
  selector: 'jhi-expiration-point',
  templateUrl: './expiration-point.component.html',
})
export class ExpirationPointComponent implements OnInit {
  expirationPoints?: IExpirationPoint[];
  isLoading = false;

  constructor(protected expirationPointService: ExpirationPointService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.expirationPointService.query().subscribe(
      (res: HttpResponse<IExpirationPoint[]>) => {
        this.isLoading = false;
        this.expirationPoints = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IExpirationPoint): number {
    return item.id!;
  }

  delete(expirationPoint: IExpirationPoint): void {
    const modalRef = this.modalService.open(ExpirationPointDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.expirationPoint = expirationPoint;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
