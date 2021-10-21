import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPointUse, PointUse } from '../point-use.model';
import { PointUseService } from '../service/point-use.service';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';
import { IPointUsageConcept } from 'app/entities/point-usage-concept/point-usage-concept.model';
import { PointUsageConceptService } from 'app/entities/point-usage-concept/service/point-usage-concept.service';

@Component({
  selector: 'jhi-point-use-update',
  templateUrl: './point-use-update.component.html',
})
export class PointUseUpdateComponent implements OnInit {
  isSaving = false;

  clientsCollection: IClient[] = [];
  pointUsageConceptsSharedCollection: IPointUsageConcept[] = [];

  editForm = this.fb.group({
    id: [],
    scoreUsed: [null, [Validators.required]],
    eventDate: [null, [Validators.required]],
    client: [],
    pointUsageConcept: [],
  });

  constructor(
    protected pointUseService: PointUseService,
    protected clientService: ClientService,
    protected pointUsageConceptService: PointUsageConceptService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pointUse }) => {
      if (pointUse.id === undefined) {
        const today = dayjs().startOf('day');
        pointUse.eventDate = today;
      }

      this.updateForm(pointUse);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pointUse = this.createFromForm();
    if (pointUse.id !== undefined) {
      this.subscribeToSaveResponse(this.pointUseService.update(pointUse));
    } else {
      this.subscribeToSaveResponse(this.pointUseService.create(pointUse));
    }
  }

  trackClientById(index: number, item: IClient): number {
    return item.id!;
  }

  trackPointUsageConceptById(index: number, item: IPointUsageConcept): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPointUse>>): void {
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

  protected updateForm(pointUse: IPointUse): void {
    this.editForm.patchValue({
      id: pointUse.id,
      scoreUsed: pointUse.scoreUsed,
      eventDate: pointUse.eventDate ? pointUse.eventDate.format(DATE_TIME_FORMAT) : null,
      client: pointUse.client,
      pointUsageConcept: pointUse.pointUsageConcept,
    });

    this.clientsCollection = this.clientService.addClientToCollectionIfMissing(this.clientsCollection, pointUse.client);
    this.pointUsageConceptsSharedCollection = this.pointUsageConceptService.addPointUsageConceptToCollectionIfMissing(
      this.pointUsageConceptsSharedCollection,
      pointUse.pointUsageConcept
    );
  }

  protected loadRelationshipsOptions(): void {
    this.clientService
      .query({ filter: 'pointuse-is-null' })
      .pipe(map((res: HttpResponse<IClient[]>) => res.body ?? []))
      .pipe(map((clients: IClient[]) => this.clientService.addClientToCollectionIfMissing(clients, this.editForm.get('client')!.value)))
      .subscribe((clients: IClient[]) => (this.clientsCollection = clients));

    this.pointUsageConceptService
      .query()
      .pipe(map((res: HttpResponse<IPointUsageConcept[]>) => res.body ?? []))
      .pipe(
        map((pointUsageConcepts: IPointUsageConcept[]) =>
          this.pointUsageConceptService.addPointUsageConceptToCollectionIfMissing(
            pointUsageConcepts,
            this.editForm.get('pointUsageConcept')!.value
          )
        )
      )
      .subscribe((pointUsageConcepts: IPointUsageConcept[]) => (this.pointUsageConceptsSharedCollection = pointUsageConcepts));
  }

  protected createFromForm(): IPointUse {
    return {
      ...new PointUse(),
      id: this.editForm.get(['id'])!.value,
      scoreUsed: this.editForm.get(['scoreUsed'])!.value,
      eventDate: this.editForm.get(['eventDate'])!.value ? dayjs(this.editForm.get(['eventDate'])!.value, DATE_TIME_FORMAT) : undefined,
      client: this.editForm.get(['client'])!.value,
      pointUsageConcept: this.editForm.get(['pointUsageConcept'])!.value,
    };
  }
}
