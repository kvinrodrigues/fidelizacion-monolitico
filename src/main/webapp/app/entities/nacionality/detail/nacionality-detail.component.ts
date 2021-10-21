import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { INacionality } from '../nacionality.model';

@Component({
  selector: 'jhi-nacionality-detail',
  templateUrl: './nacionality-detail.component.html',
})
export class NacionalityDetailComponent implements OnInit {
  nacionality: INacionality | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ nacionality }) => {
      this.nacionality = nacionality;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
