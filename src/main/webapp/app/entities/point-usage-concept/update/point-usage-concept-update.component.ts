import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPointUsageConcept, PointUsageConcept } from '../point-usage-concept.model';
import { PointUsageConceptService } from '../service/point-usage-concept.service';

@Component({
  selector: 'jhi-point-usage-concept-update',
  templateUrl: './point-usage-concept-update.component.html',
})
export class PointUsageConceptUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    description: [],
    requiredPoints: [],
  });

  constructor(
    protected pointUsageConceptService: PointUsageConceptService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pointUsageConcept }) => {
      this.updateForm(pointUsageConcept);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pointUsageConcept = this.createFromForm();
    if (pointUsageConcept.id !== undefined) {
      this.subscribeToSaveResponse(this.pointUsageConceptService.update(pointUsageConcept));
    } else {
      this.subscribeToSaveResponse(this.pointUsageConceptService.create(pointUsageConcept));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPointUsageConcept>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(pointUsageConcept: IPointUsageConcept): void {
    this.editForm.patchValue({
      id: pointUsageConcept.id,
      description: pointUsageConcept.description,
      requiredPoints: pointUsageConcept.requiredPoints,
    });
  }

  protected createFromForm(): IPointUsageConcept {
    return {
      ...new PointUsageConcept(),
      id: this.editForm.get(['id'])!.value,
      description: this.editForm.get(['description'])!.value,
      requiredPoints: this.editForm.get(['requiredPoints'])!.value,
    };
  }
}
