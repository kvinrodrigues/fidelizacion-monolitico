jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ExpirationPointService } from '../service/expiration-point.service';
import { IExpirationPoint, ExpirationPoint } from '../expiration-point.model';

import { ExpirationPointUpdateComponent } from './expiration-point-update.component';

describe('Component Tests', () => {
  describe('ExpirationPoint Management Update Component', () => {
    let comp: ExpirationPointUpdateComponent;
    let fixture: ComponentFixture<ExpirationPointUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let expirationPointService: ExpirationPointService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ExpirationPointUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ExpirationPointUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ExpirationPointUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      expirationPointService = TestBed.inject(ExpirationPointService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const expirationPoint: IExpirationPoint = { id: 456 };

        activatedRoute.data = of({ expirationPoint });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(expirationPoint));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ExpirationPoint>>();
        const expirationPoint = { id: 123 };
        jest.spyOn(expirationPointService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ expirationPoint });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: expirationPoint }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(expirationPointService.update).toHaveBeenCalledWith(expirationPoint);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ExpirationPoint>>();
        const expirationPoint = new ExpirationPoint();
        jest.spyOn(expirationPointService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ expirationPoint });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: expirationPoint }));
        saveSubject.complete();

        // THEN
        expect(expirationPointService.create).toHaveBeenCalledWith(expirationPoint);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<ExpirationPoint>>();
        const expirationPoint = { id: 123 };
        jest.spyOn(expirationPointService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ expirationPoint });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(expirationPointService.update).toHaveBeenCalledWith(expirationPoint);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
