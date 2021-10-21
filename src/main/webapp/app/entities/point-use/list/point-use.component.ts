import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPointUse } from '../point-use.model';
import { PointUseService } from '../service/point-use.service';
import { PointUseDeleteDialogComponent } from '../delete/point-use-delete-dialog.component';

@Component({
  selector: 'jhi-point-use',
  templateUrl: './point-use.component.html',
})
export class PointUseComponent implements OnInit {
  pointUses?: IPointUse[];
  isLoading = false;

  constructor(protected pointUseService: PointUseService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.pointUseService.query().subscribe(
      (res: HttpResponse<IPointUse[]>) => {
        this.isLoading = false;
        this.pointUses = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPointUse): number {
    return item.id!;
  }

  delete(pointUse: IPointUse): void {
    const modalRef = this.modalService.open(PointUseDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.pointUse = pointUse;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
