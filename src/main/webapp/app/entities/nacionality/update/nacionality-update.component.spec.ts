jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { NacionalityService } from '../service/nacionality.service';
import { INacionality, Nacionality } from '../nacionality.model';

import { NacionalityUpdateComponent } from './nacionality-update.component';

describe('Component Tests', () => {
  describe('Nacionality Management Update Component', () => {
    let comp: NacionalityUpdateComponent;
    let fixture: ComponentFixture<NacionalityUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let nacionalityService: NacionalityService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [NacionalityUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(NacionalityUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(NacionalityUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      nacionalityService = TestBed.inject(NacionalityService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const nacionality: INacionality = { id: 456 };

        activatedRoute.data = of({ nacionality });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(nacionality));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Nacionality>>();
        const nacionality = { id: 123 };
        jest.spyOn(nacionalityService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ nacionality });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: nacionality }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(nacionalityService.update).toHaveBeenCalledWith(nacionality);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Nacionality>>();
        const nacionality = new Nacionality();
        jest.spyOn(nacionalityService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ nacionality });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: nacionality }));
        saveSubject.complete();

        // THEN
        expect(nacionalityService.create).toHaveBeenCalledWith(nacionality);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Nacionality>>();
        const nacionality = { id: 123 };
        jest.spyOn(nacionalityService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ nacionality });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(nacionalityService.update).toHaveBeenCalledWith(nacionality);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
