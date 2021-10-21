import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBagOfPoint } from '../bag-of-point.model';
import { BagOfPointService } from '../service/bag-of-point.service';
import { BagOfPointDeleteDialogComponent } from '../delete/bag-of-point-delete-dialog.component';

@Component({
  selector: 'jhi-bag-of-point',
  templateUrl: './bag-of-point.component.html',
})
export class BagOfPointComponent implements OnInit {
  bagOfPoints?: IBagOfPoint[];
  isLoading = false;

  constructor(protected bagOfPointService: BagOfPointService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.bagOfPointService.query().subscribe(
      (res: HttpResponse<IBagOfPoint[]>) => {
        this.isLoading = false;
        this.bagOfPoints = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IBagOfPoint): number {
    return item.id!;
  }

  delete(bagOfPoint: IBagOfPoint): void {
    const modalRef = this.modalService.open(BagOfPointDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.bagOfPoint = bagOfPoint;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
