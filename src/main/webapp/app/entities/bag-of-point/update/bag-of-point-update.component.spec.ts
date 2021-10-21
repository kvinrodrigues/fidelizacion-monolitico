jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { BagOfPointService } from '../service/bag-of-point.service';
import { IBagOfPoint, BagOfPoint } from '../bag-of-point.model';

import { BagOfPointUpdateComponent } from './bag-of-point-update.component';

describe('Component Tests', () => {
  describe('BagOfPoint Management Update Component', () => {
    let comp: BagOfPointUpdateComponent;
    let fixture: ComponentFixture<BagOfPointUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let bagOfPointService: BagOfPointService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [BagOfPointUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(BagOfPointUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BagOfPointUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      bagOfPointService = TestBed.inject(BagOfPointService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const bagOfPoint: IBagOfPoint = { id: 456 };

        activatedRoute.data = of({ bagOfPoint });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(bagOfPoint));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<BagOfPoint>>();
        const bagOfPoint = { id: 123 };
        jest.spyOn(bagOfPointService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ bagOfPoint });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: bagOfPoint }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(bagOfPointService.update).toHaveBeenCalledWith(bagOfPoint);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<BagOfPoint>>();
        const bagOfPoint = new BagOfPoint();
        jest.spyOn(bagOfPointService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ bagOfPoint });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: bagOfPoint }));
        saveSubject.complete();

        // THEN
        expect(bagOfPointService.create).toHaveBeenCalledWith(bagOfPoint);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<BagOfPoint>>();
        const bagOfPoint = { id: 123 };
        jest.spyOn(bagOfPointService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ bagOfPoint });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(bagOfPointService.update).toHaveBeenCalledWith(bagOfPoint);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
