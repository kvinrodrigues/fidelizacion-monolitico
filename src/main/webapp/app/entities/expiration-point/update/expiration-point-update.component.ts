import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IExpirationPoint, ExpirationPoint } from '../expiration-point.model';
import { ExpirationPointService } from '../service/expiration-point.service';

@Component({
  selector: 'jhi-expiration-point-update',
  templateUrl: './expiration-point-update.component.html',
})
export class ExpirationPointUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    validityStartDate: [null, [Validators.required]],
    validityEndDate: [null, [Validators.required]],
    scoreDurationDays: [null, [Validators.required]],
  });

  constructor(
    protected expirationPointService: ExpirationPointService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ expirationPoint }) => {
      if (expirationPoint.id === undefined) {
        const today = dayjs().startOf('day');
        expirationPoint.validityStartDate = today;
        expirationPoint.validityEndDate = today;
      }

      this.updateForm(expirationPoint);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const expirationPoint = this.createFromForm();
    if (expirationPoint.id !== undefined) {
      this.subscribeToSaveResponse(this.expirationPointService.update(expirationPoint));
    } else {
      this.subscribeToSaveResponse(this.expirationPointService.create(expirationPoint));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExpirationPoint>>): void {
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

  protected updateForm(expirationPoint: IExpirationPoint): void {
    this.editForm.patchValue({
      id: expirationPoint.id,
      validityStartDate: expirationPoint.validityStartDate ? expirationPoint.validityStartDate.format(DATE_TIME_FORMAT) : null,
      validityEndDate: expirationPoint.validityEndDate ? expirationPoint.validityEndDate.format(DATE_TIME_FORMAT) : null,
      scoreDurationDays: expirationPoint.scoreDurationDays,
    });
  }

  protected createFromForm(): IExpirationPoint {
    return {
      ...new ExpirationPoint(),
      id: this.editForm.get(['id'])!.value,
      validityStartDate: this.editForm.get(['validityStartDate'])!.value
        ? dayjs(this.editForm.get(['validityStartDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      validityEndDate: this.editForm.get(['validityEndDate'])!.value
        ? dayjs(this.editForm.get(['validityEndDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      scoreDurationDays: this.editForm.get(['scoreDurationDays'])!.value,
    };
  }
}
