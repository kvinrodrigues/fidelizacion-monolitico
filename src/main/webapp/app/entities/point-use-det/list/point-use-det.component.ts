import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPointUseDet } from '../point-use-det.model';
import { PointUseDetService } from '../service/point-use-det.service';
import { PointUseDetDeleteDialogComponent } from '../delete/point-use-det-delete-dialog.component';

@Component({
  selector: 'jhi-point-use-det',
  templateUrl: './point-use-det.component.html',
})
export class PointUseDetComponent implements OnInit {
  pointUseDets?: IPointUseDet[];
  isLoading = false;

  constructor(protected pointUseDetService: PointUseDetService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.pointUseDetService.query().subscribe(
      (res: HttpResponse<IPointUseDet[]>) => {
        this.isLoading = false;
        this.pointUseDets = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPointUseDet): number {
    return item.id!;
  }

  delete(pointUseDet: IPointUseDet): void {
    const modalRef = this.modalService.open(PointUseDetDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.pointUseDet = pointUseDet;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
