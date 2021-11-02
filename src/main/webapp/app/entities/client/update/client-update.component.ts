import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IClient, Client } from '../client.model';
import { ClientService } from '../service/client.service';
import { IDocumentType } from 'app/entities/document-type/document-type.model';
import { DocumentTypeService } from 'app/entities/document-type/service/document-type.service';
import { INacionality } from 'app/entities/nacionality/nacionality.model';
import { NacionalityService } from 'app/entities/nacionality/service/nacionality.service';

@Component({
  selector: 'jhi-client-update',
  templateUrl: './client-update.component.html',
})
export class ClientUpdateComponent implements OnInit {
  isSaving = false;

  documentTypesSharedCollection: IDocumentType[] = [];
  nacionalitiesSharedCollection: INacionality[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    lastName: [null, [Validators.required]],
    documentNumber: [null, [Validators.required]],
    email: [null, [Validators.required]],
    phoneNumber: [null, [Validators.required]],
    birthDate: [null, [Validators.required]],
    documentType: [],
    nacionality: [],
  });

  constructor(
    protected clientService: ClientService,
    protected documentTypeService: DocumentTypeService,
    protected nacionalityService: NacionalityService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ client }) => {
      if (client.id === undefined) {
        const today = dayjs().startOf('day');
        client.birthDate = today;
      }

      this.updateForm(client);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const client = this.createFromForm();
    if (client.id !== undefined) {
      this.subscribeToSaveResponse(this.clientService.update(client));
    } else {
      this.subscribeToSaveResponse(this.clientService.create(client));
    }
  }

  trackDocumentTypeById(index: number, item: IDocumentType): number {
    return item.id!;
  }

  trackNacionalityById(index: number, item: INacionality): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClient>>): void {
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

  protected updateForm(client: IClient): void {
    this.editForm.patchValue({
      id: client.id,
      name: client.name,
      lastName: client.lastName,
      documentNumber: client.documentNumber,
      email: client.email,
      phoneNumber: client.phoneNumber,
      birthDate: client.birthDate ? client.birthDate.format(DATE_TIME_FORMAT) : null,
      documentType: client.documentType,
      nacionality: client.nacionality,
    });

    this.documentTypesSharedCollection = this.documentTypeService.addDocumentTypeToCollectionIfMissing(
      this.documentTypesSharedCollection,
      client.documentType
    );
    this.nacionalitiesSharedCollection = this.nacionalityService.addNacionalityToCollectionIfMissing(
      this.nacionalitiesSharedCollection,
      client.nacionality
    );
  }

  protected loadRelationshipsOptions(): void {
    this.documentTypeService
      .query()
      .pipe(map((res: HttpResponse<IDocumentType[]>) => res.body ?? []))
      .pipe(
        map((documentTypes: IDocumentType[]) =>
          this.documentTypeService.addDocumentTypeToCollectionIfMissing(documentTypes, this.editForm.get('documentType')!.value)
        )
      )
      .subscribe((documentTypes: IDocumentType[]) => (this.documentTypesSharedCollection = documentTypes));

    this.nacionalityService
      .query()
      .pipe(map((res: HttpResponse<INacionality[]>) => res.body ?? []))
      .pipe(
        map((nacionalities: INacionality[]) =>
          this.nacionalityService.addNacionalityToCollectionIfMissing(nacionalities, this.editForm.get('nacionality')!.value)
        )
      )
      .subscribe((nacionalities: INacionality[]) => (this.nacionalitiesSharedCollection = nacionalities));
  }

  protected createFromForm(): IClient {
    return {
      ...new Client(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      lastName: this.editForm.get(['lastName'])!.value,
      documentNumber: this.editForm.get(['documentNumber'])!.value,
      email: this.editForm.get(['email'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      birthDate: this.editForm.get(['birthDate'])!.value ? dayjs(this.editForm.get(['birthDate'])!.value, DATE_TIME_FORMAT) : undefined,
      documentType: this.editForm.get(['documentType'])!.value,
      nacionality: this.editForm.get(['nacionality'])!.value,
    };
  }
}
