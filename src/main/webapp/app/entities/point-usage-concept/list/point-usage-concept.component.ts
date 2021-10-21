import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPointUsageConcept } from '../point-usage-concept.model';
import { PointUsageConceptService } from '../service/point-usage-concept.service';
import { PointUsageConceptDeleteDialogComponent } from '../delete/point-usage-concept-delete-dialog.component';

@Component({
  selector: 'jhi-point-usage-concept',
  templateUrl: './point-usage-concept.component.html',
})
export class PointUsageConceptComponent implements OnInit {
  pointUsageConcepts?: IPointUsageConcept[];
  isLoading = false;

  constructor(protected pointUsageConceptService: PointUsageConceptService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.pointUsageConceptService.query().subscribe(
      (res: HttpResponse<IPointUsageConcept[]>) => {
        this.isLoading = false;
        this.pointUsageConcepts = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPointUsageConcept): number {
    return item.id!;
  }

  delete(pointUsageConcept: IPointUsageConcept): void {
    const modalRef = this.modalService.open(PointUsageConceptDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.pointUsageConcept = pointUsageConcept;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
