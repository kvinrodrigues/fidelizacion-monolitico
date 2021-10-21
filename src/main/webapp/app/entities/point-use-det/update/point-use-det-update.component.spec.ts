jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PointUseDetService } from '../service/point-use-det.service';
import { IPointUseDet, PointUseDet } from '../point-use-det.model';
import { IPointUse } from 'app/entities/point-use/point-use.model';
import { PointUseService } from 'app/entities/point-use/service/point-use.service';
import { IBagOfPoint } from 'app/entities/bag-of-point/bag-of-point.model';
import { BagOfPointService } from 'app/entities/bag-of-point/service/bag-of-point.service';

import { PointUseDetUpdateComponent } from './point-use-det-update.component';

describe('Component Tests', () => {
  describe('PointUseDet Management Update Component', () => {
    let comp: PointUseDetUpdateComponent;
    let fixture: ComponentFixture<PointUseDetUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let pointUseDetService: PointUseDetService;
    let pointUseService: PointUseService;
    let bagOfPointService: BagOfPointService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PointUseDetUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PointUseDetUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PointUseDetUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      pointUseDetService = TestBed.inject(PointUseDetService);
      pointUseService = TestBed.inject(PointUseService);
      bagOfPointService = TestBed.inject(BagOfPointService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call PointUse query and add missing value', () => {
        const pointUseDet: IPointUseDet = { id: 456 };
        const pointUse: IPointUse = { id: 96341 };
        pointUseDet.pointUse = pointUse;

        const pointUseCollection: IPointUse[] = [{ id: 36979 }];
        jest.spyOn(pointUseService, 'query').mockReturnValue(of(new HttpResponse({ body: pointUseCollection })));
        const additionalPointUses = [pointUse];
        const expectedCollection: IPointUse[] = [...additionalPointUses, ...pointUseCollection];
        jest.spyOn(pointUseService, 'addPointUseToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ pointUseDet });
        comp.ngOnInit();

        expect(pointUseService.query).toHaveBeenCalled();
        expect(pointUseService.addPointUseToCollectionIfMissing).toHaveBeenCalledWith(pointUseCollection, ...additionalPointUses);
        expect(comp.pointUsesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call BagOfPoint query and add missing value', () => {
        const pointUseDet: IPointUseDet = { id: 456 };
        const bagOfPoint: IBagOfPoint = { id: 76299 };
        pointUseDet.bagOfPoint = bagOfPoint;

        const bagOfPointCollection: IBagOfPoint[] = [{ id: 45732 }];
        jest.spyOn(bagOfPointService, 'query').mockReturnValue(of(new HttpResponse({ body: bagOfPointCollection })));
        const additionalBagOfPoints = [bagOfPoint];
        const expectedCollection: IBagOfPoint[] = [...additionalBagOfPoints, ...bagOfPointCollection];
        jest.spyOn(bagOfPointService, 'addBagOfPointToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ pointUseDet });
        comp.ngOnInit();

        expect(bagOfPointService.query).toHaveBeenCalled();
        expect(bagOfPointService.addBagOfPointToCollectionIfMissing).toHaveBeenCalledWith(bagOfPointCollection, ...additionalBagOfPoints);
        expect(comp.bagOfPointsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const pointUseDet: IPointUseDet = { id: 456 };
        const pointUse: IPointUse = { id: 40882 };
        pointUseDet.pointUse = pointUse;
        const bagOfPoint: IBagOfPoint = { id: 1695 };
        pointUseDet.bagOfPoint = bagOfPoint;

        activatedRoute.data = of({ pointUseDet });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(pointUseDet));
        expect(comp.pointUsesSharedCollection).toContain(pointUse);
        expect(comp.bagOfPointsSharedCollection).toContain(bagOfPoint);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PointUseDet>>();
        const pointUseDet = { id: 123 };
        jest.spyOn(pointUseDetService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ pointUseDet });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: pointUseDet }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(pointUseDetService.update).toHaveBeenCalledWith(pointUseDet);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PointUseDet>>();
        const pointUseDet = new PointUseDet();
        jest.spyOn(pointUseDetService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ pointUseDet });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: pointUseDet }));
        saveSubject.complete();

        // THEN
        expect(pointUseDetService.create).toHaveBeenCalledWith(pointUseDet);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PointUseDet>>();
        const pointUseDet = { id: 123 };
        jest.spyOn(pointUseDetService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ pointUseDet });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(pointUseDetService.update).toHaveBeenCalledWith(pointUseDet);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackPointUseById', () => {
        it('Should return tracked PointUse primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPointUseById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackBagOfPointById', () => {
        it('Should return tracked BagOfPoint primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackBagOfPointById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
