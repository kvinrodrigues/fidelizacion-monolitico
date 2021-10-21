import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPointUseDet, PointUseDet } from '../point-use-det.model';
import { PointUseDetService } from '../service/point-use-det.service';
import { IPointUse } from 'app/entities/point-use/point-use.model';
import { PointUseService } from 'app/entities/point-use/service/point-use.service';
import { IBagOfPoint } from 'app/entities/bag-of-point/bag-of-point.model';
import { BagOfPointService } from 'app/entities/bag-of-point/service/bag-of-point.service';

@Component({
  selector: 'jhi-point-use-det-update',
  templateUrl: './point-use-det-update.component.html',
})
export class PointUseDetUpdateComponent implements OnInit {
  isSaving = false;

  pointUsesSharedCollection: IPointUse[] = [];
  bagOfPointsSharedCollection: IBagOfPoint[] = [];

  editForm = this.fb.group({
    id: [],
    scoreUsed: [null, [Validators.required]],
    pointUse: [],
    bagOfPoint: [],
  });

  constructor(
    protected pointUseDetService: PointUseDetService,
    protected pointUseService: PointUseService,
    protected bagOfPointService: BagOfPointService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pointUseDet }) => {
      this.updateForm(pointUseDet);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pointUseDet = this.createFromForm();
    if (pointUseDet.id !== undefined) {
      this.subscribeToSaveResponse(this.pointUseDetService.update(pointUseDet));
    } else {
      this.subscribeToSaveResponse(this.pointUseDetService.create(pointUseDet));
    }
  }

  trackPointUseById(index: number, item: IPointUse): number {
    return item.id!;
  }

  trackBagOfPointById(index: number, item: IBagOfPoint): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPointUseDet>>): void {
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

  protected updateForm(pointUseDet: IPointUseDet): void {
    this.editForm.patchValue({
      id: pointUseDet.id,
      scoreUsed: pointUseDet.scoreUsed,
      pointUse: pointUseDet.pointUse,
      bagOfPoint: pointUseDet.bagOfPoint,
    });

    this.pointUsesSharedCollection = this.pointUseService.addPointUseToCollectionIfMissing(
      this.pointUsesSharedCollection,
      pointUseDet.pointUse
    );
    this.bagOfPointsSharedCollection = this.bagOfPointService.addBagOfPointToCollectionIfMissing(
      this.bagOfPointsSharedCollection,
      pointUseDet.bagOfPoint
    );
  }

  protected loadRelationshipsOptions(): void {
    this.pointUseService
      .query()
      .pipe(map((res: HttpResponse<IPointUse[]>) => res.body ?? []))
      .pipe(
        map((pointUses: IPointUse[]) =>
          this.pointUseService.addPointUseToCollectionIfMissing(pointUses, this.editForm.get('pointUse')!.value)
        )
      )
      .subscribe((pointUses: IPointUse[]) => (this.pointUsesSharedCollection = pointUses));

    this.bagOfPointService
      .query()
      .pipe(map((res: HttpResponse<IBagOfPoint[]>) => res.body ?? []))
      .pipe(
        map((bagOfPoints: IBagOfPoint[]) =>
          this.bagOfPointService.addBagOfPointToCollectionIfMissing(bagOfPoints, this.editForm.get('bagOfPoint')!.value)
        )
      )
      .subscribe((bagOfPoints: IBagOfPoint[]) => (this.bagOfPointsSharedCollection = bagOfPoints));
  }

  protected createFromForm(): IPointUseDet {
    return {
      ...new PointUseDet(),
      id: this.editForm.get(['id'])!.value,
      scoreUsed: this.editForm.get(['scoreUsed'])!.value,
      pointUse: this.editForm.get(['pointUse'])!.value,
      bagOfPoint: this.editForm.get(['bagOfPoint'])!.value,
    };
  }
}
