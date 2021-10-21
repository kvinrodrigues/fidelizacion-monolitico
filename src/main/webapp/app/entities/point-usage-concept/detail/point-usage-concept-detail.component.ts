import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPointUsageConcept } from '../point-usage-concept.model';

@Component({
  selector: 'jhi-point-usage-concept-detail',
  templateUrl: './point-usage-concept-detail.component.html',
})
export class PointUsageConceptDetailComponent implements OnInit {
  pointUsageConcept: IPointUsageConcept | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pointUsageConcept }) => {
      this.pointUsageConcept = pointUsageConcept;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
