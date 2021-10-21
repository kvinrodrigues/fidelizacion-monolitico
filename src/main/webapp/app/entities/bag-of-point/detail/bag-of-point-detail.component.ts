import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBagOfPoint } from '../bag-of-point.model';

@Component({
  selector: 'jhi-bag-of-point-detail',
  templateUrl: './bag-of-point-detail.component.html',
})
export class BagOfPointDetailComponent implements OnInit {
  bagOfPoint: IBagOfPoint | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bagOfPoint }) => {
      this.bagOfPoint = bagOfPoint;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
