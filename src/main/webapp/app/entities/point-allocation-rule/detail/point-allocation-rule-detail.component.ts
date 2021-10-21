import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPointAllocationRule } from '../point-allocation-rule.model';

@Component({
  selector: 'jhi-point-allocation-rule-detail',
  templateUrl: './point-allocation-rule-detail.component.html',
})
export class PointAllocationRuleDetailComponent implements OnInit {
  pointAllocationRule: IPointAllocationRule | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pointAllocationRule }) => {
      this.pointAllocationRule = pointAllocationRule;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
