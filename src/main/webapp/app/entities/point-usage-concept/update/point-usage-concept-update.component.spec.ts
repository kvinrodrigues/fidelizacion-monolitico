jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PointUsageConceptService } from '../service/point-usage-concept.service';
import { IPointUsageConcept, PointUsageConcept } from '../point-usage-concept.model';

import { PointUsageConceptUpdateComponent } from './point-usage-concept-update.component';

describe('Component Tests', () => {
  describe('PointUsageConcept Management Update Component', () => {
    let comp: PointUsageConceptUpdateComponent;
    let fixture: ComponentFixture<PointUsageConceptUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let pointUsageConceptService: PointUsageConceptService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PointUsageConceptUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PointUsageConceptUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PointUsageConceptUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      pointUsageConceptService = TestBed.inject(PointUsageConceptService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const pointUsageConcept: IPointUsageConcept = { id: 456 };

        activatedRoute.data = of({ pointUsageConcept });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(pointUsageConcept));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PointUsageConcept>>();
        const pointUsageConcept = { id: 123 };
        jest.spyOn(pointUsageConceptService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ pointUsageConcept });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: pointUsageConcept }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(pointUsageConceptService.update).toHaveBeenCalledWith(pointUsageConcept);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PointUsageConcept>>();
        const pointUsageConcept = new PointUsageConcept();
        jest.spyOn(pointUsageConceptService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ pointUsageConcept });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: pointUsageConcept }));
        saveSubject.complete();

        // THEN
        expect(pointUsageConceptService.create).toHaveBeenCalledWith(pointUsageConcept);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PointUsageConcept>>();
        const pointUsageConcept = { id: 123 };
        jest.spyOn(pointUsageConceptService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ pointUsageConcept });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(pointUsageConceptService.update).toHaveBeenCalledWith(pointUsageConcept);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
