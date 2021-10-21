import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPointUse } from '../point-use.model';

@Component({
  selector: 'jhi-point-use-detail',
  templateUrl: './point-use-detail.component.html',
})
export class PointUseDetailComponent implements OnInit {
  pointUse: IPointUse | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pointUse }) => {
      this.pointUse = pointUse;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
