import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IExpirationPoint } from '../expiration-point.model';

@Component({
  selector: 'jhi-expiration-point-detail',
  templateUrl: './expiration-point-detail.component.html',
})
export class ExpirationPointDetailComponent implements OnInit {
  expirationPoint: IExpirationPoint | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ expirationPoint }) => {
      this.expirationPoint = expirationPoint;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
