import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IBagOfPoint, BagOfPoint } from '../bag-of-point.model';
import { BagOfPointService } from '../service/bag-of-point.service';

@Component({
  selector: 'jhi-bag-of-point-update',
  templateUrl: './bag-of-point-update.component.html',
})
export class BagOfPointUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    asignationDate: [null, [Validators.required]],
    expirationDate: [null, [Validators.required]],
    assignedScore: [null, [Validators.required]],
    scoreUsed: [null, [Validators.required]],
    scoreBalance: [null, [Validators.required]],
    operationAmount: [null, [Validators.required]],
  });

  constructor(protected bagOfPointService: BagOfPointService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bagOfPoint }) => {
      if (bagOfPoint.id === undefined) {
        const today = dayjs().startOf('day');
        bagOfPoint.asignationDate = today;
        bagOfPoint.expirationDate = today;
      }

      this.updateForm(bagOfPoint);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bagOfPoint = this.createFromForm();
    if (bagOfPoint.id !== undefined) {
      this.subscribeToSaveResponse(this.bagOfPointService.update(bagOfPoint));
    } else {
      this.subscribeToSaveResponse(this.bagOfPointService.create(bagOfPoint));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBagOfPoint>>): void {
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

  protected updateForm(bagOfPoint: IBagOfPoint): void {
    this.editForm.patchValue({
      id: bagOfPoint.id,
      asignationDate: bagOfPoint.asignationDate ? bagOfPoint.asignationDate.format(DATE_TIME_FORMAT) : null,
      expirationDate: bagOfPoint.expirationDate ? bagOfPoint.expirationDate.format(DATE_TIME_FORMAT) : null,
      assignedScore: bagOfPoint.assignedScore,
      scoreUsed: bagOfPoint.scoreUsed,
      scoreBalance: bagOfPoint.scoreBalance,
      operationAmount: bagOfPoint.operationAmount,
    });
  }

  protected createFromForm(): IBagOfPoint {
    return {
      ...new BagOfPoint(),
      id: this.editForm.get(['id'])!.value,
      asignationDate: this.editForm.get(['asignationDate'])!.value
        ? dayjs(this.editForm.get(['asignationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      expirationDate: this.editForm.get(['expirationDate'])!.value
        ? dayjs(this.editForm.get(['expirationDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      assignedScore: this.editForm.get(['assignedScore'])!.value,
      scoreUsed: this.editForm.get(['scoreUsed'])!.value,
      scoreBalance: this.editForm.get(['scoreBalance'])!.value,
      operationAmount: this.editForm.get(['operationAmount'])!.value,
    };
  }
}
