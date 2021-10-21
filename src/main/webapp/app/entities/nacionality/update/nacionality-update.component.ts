import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { INacionality, Nacionality } from '../nacionality.model';
import { NacionalityService } from '../service/nacionality.service';

@Component({
  selector: 'jhi-nacionality-update',
  templateUrl: './nacionality-update.component.html',
})
export class NacionalityUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    description: [null, [Validators.required]],
  });

  constructor(protected nacionalityService: NacionalityService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ nacionality }) => {
      this.updateForm(nacionality);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const nacionality = this.createFromForm();
    if (nacionality.id !== undefined) {
      this.subscribeToSaveResponse(this.nacionalityService.update(nacionality));
    } else {
      this.subscribeToSaveResponse(this.nacionalityService.create(nacionality));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INacionality>>): void {
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

  protected updateForm(nacionality: INacionality): void {
    this.editForm.patchValue({
      id: nacionality.id,
      name: nacionality.name,
      description: nacionality.description,
    });
  }

  protected createFromForm(): INacionality {
    return {
      ...new Nacionality(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
    };
  }
}
