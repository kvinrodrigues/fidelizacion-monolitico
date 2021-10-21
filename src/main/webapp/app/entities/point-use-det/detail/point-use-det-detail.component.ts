import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPointUseDet } from '../point-use-det.model';

@Component({
  selector: 'jhi-point-use-det-detail',
  templateUrl: './point-use-det-detail.component.html',
})
export class PointUseDetDetailComponent implements OnInit {
  pointUseDet: IPointUseDet | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pointUseDet }) => {
      this.pointUseDet = pointUseDet;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
