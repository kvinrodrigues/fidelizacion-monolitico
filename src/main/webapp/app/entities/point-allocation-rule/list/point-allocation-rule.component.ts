import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPointAllocationRule } from '../point-allocation-rule.model';
import { PointAllocationRuleService } from '../service/point-allocation-rule.service';
import { PointAllocationRuleDeleteDialogComponent } from '../delete/point-allocation-rule-delete-dialog.component';

@Component({
  selector: 'jhi-point-allocation-rule',
  templateUrl: './point-allocation-rule.component.html',
})
export class PointAllocationRuleComponent implements OnInit {
  pointAllocationRules?: IPointAllocationRule[];
  isLoading = false;

  constructor(protected pointAllocationRuleService: PointAllocationRuleService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.pointAllocationRuleService.query().subscribe(
      (res: HttpResponse<IPointAllocationRule[]>) => {
        this.isLoading = false;
        this.pointAllocationRules = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPointAllocationRule): number {
    return item.id!;
  }

  delete(pointAllocationRule: IPointAllocationRule): void {
    const modalRef = this.modalService.open(PointAllocationRuleDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.pointAllocationRule = pointAllocationRule;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
