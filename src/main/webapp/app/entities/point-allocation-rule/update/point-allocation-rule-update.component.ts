import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPointAllocationRule, PointAllocationRule } from '../point-allocation-rule.model';
import { PointAllocationRuleService } from '../service/point-allocation-rule.service';

@Component({
  selector: 'jhi-point-allocation-rule-update',
  templateUrl: './point-allocation-rule-update.component.html',
})
export class PointAllocationRuleUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    lowerLimit: [],
    upperLimit: [],
    equivalenceOfAPoint: [null, [Validators.required]],
  });

  constructor(
    protected pointAllocationRuleService: PointAllocationRuleService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pointAllocationRule }) => {
      this.updateForm(pointAllocationRule);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pointAllocationRule = this.createFromForm();
    if (pointAllocationRule.id !== undefined) {
      this.subscribeToSaveResponse(this.pointAllocationRuleService.update(pointAllocationRule));
    } else {
      this.subscribeToSaveResponse(this.pointAllocationRuleService.create(pointAllocationRule));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPointAllocationRule>>): void {
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

  protected updateForm(pointAllocationRule: IPointAllocationRule): void {
    this.editForm.patchValue({
      id: pointAllocationRule.id,
      lowerLimit: pointAllocationRule.lowerLimit,
      upperLimit: pointAllocationRule.upperLimit,
      equivalenceOfAPoint: pointAllocationRule.equivalenceOfAPoint,
    });
  }

  protected createFromForm(): IPointAllocationRule {
    return {
      ...new PointAllocationRule(),
      id: this.editForm.get(['id'])!.value,
      lowerLimit: this.editForm.get(['lowerLimit'])!.value,
      upperLimit: this.editForm.get(['upperLimit'])!.value,
      equivalenceOfAPoint: this.editForm.get(['equivalenceOfAPoint'])!.value,
    };
  }
}
