jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PointUseService } from '../service/point-use.service';
import { IPointUse, PointUse } from '../point-use.model';
import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';
import { IPointUsageConcept } from 'app/entities/point-usage-concept/point-usage-concept.model';
import { PointUsageConceptService } from 'app/entities/point-usage-concept/service/point-usage-concept.service';

import { PointUseUpdateComponent } from './point-use-update.component';

describe('Component Tests', () => {
  describe('PointUse Management Update Component', () => {
    let comp: PointUseUpdateComponent;
    let fixture: ComponentFixture<PointUseUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let pointUseService: PointUseService;
    let clientService: ClientService;
    let pointUsageConceptService: PointUsageConceptService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PointUseUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PointUseUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PointUseUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      pointUseService = TestBed.inject(PointUseService);
      clientService = TestBed.inject(ClientService);
      pointUsageConceptService = TestBed.inject(PointUsageConceptService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Client query and add missing value', () => {
        const pointUse: IPointUse = { id: 456 };
        const client: IClient = { id: 78368 };
        pointUse.client = client;

        const clientCollection: IClient[] = [{ id: 11675 }];
        jest.spyOn(clientService, 'query').mockReturnValue(of(new HttpResponse({ body: clientCollection })));
        const additionalClients = [client];
        const expectedCollection: IClient[] = [...additionalClients, ...clientCollection];
        jest.spyOn(clientService, 'addClientToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ pointUse });
        comp.ngOnInit();

        expect(clientService.query).toHaveBeenCalled();
        expect(clientService.addClientToCollectionIfMissing).toHaveBeenCalledWith(clientCollection, ...additionalClients);
        expect(comp.clientsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call PointUsageConcept query and add missing value', () => {
        const pointUse: IPointUse = { id: 456 };
        const pointUsageConcept: IPointUsageConcept = { id: 25615 };
        pointUse.pointUsageConcept = pointUsageConcept;

        const pointUsageConceptCollection: IPointUsageConcept[] = [{ id: 5613 }];
        jest.spyOn(pointUsageConceptService, 'query').mockReturnValue(of(new HttpResponse({ body: pointUsageConceptCollection })));
        const additionalPointUsageConcepts = [pointUsageConcept];
        const expectedCollection: IPointUsageConcept[] = [...additionalPointUsageConcepts, ...pointUsageConceptCollection];
        jest.spyOn(pointUsageConceptService, 'addPointUsageConceptToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ pointUse });
        comp.ngOnInit();

        expect(pointUsageConceptService.query).toHaveBeenCalled();
        expect(pointUsageConceptService.addPointUsageConceptToCollectionIfMissing).toHaveBeenCalledWith(
          pointUsageConceptCollection,
          ...additionalPointUsageConcepts
        );
        expect(comp.pointUsageConceptsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const pointUse: IPointUse = { id: 456 };
        const client: IClient = { id: 17787 };
        pointUse.client = client;
        const pointUsageConcept: IPointUsageConcept = { id: 3620 };
        pointUse.pointUsageConcept = pointUsageConcept;

        activatedRoute.data = of({ pointUse });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(pointUse));
        expect(comp.clientsSharedCollection).toContain(client);
        expect(comp.pointUsageConceptsSharedCollection).toContain(pointUsageConcept);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PointUse>>();
        const pointUse = { id: 123 };
        jest.spyOn(pointUseService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ pointUse });
        comp.ngOnInit();

        // WHEN
        comp.create();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: pointUse }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(pointUseService.update).toHaveBeenCalledWith(pointUse);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PointUse>>();
        const pointUse = new PointUse();
        jest.spyOn(pointUseService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ pointUse });
        comp.ngOnInit();

        // WHEN
        comp.create();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: pointUse }));
        saveSubject.complete();

        // THEN
        expect(pointUseService.create).toHaveBeenCalledWith(pointUse);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PointUse>>();
        const pointUse = { id: 123 };
        jest.spyOn(pointUseService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ pointUse });
        comp.ngOnInit();

        // WHEN
        comp.create();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(pointUseService.update).toHaveBeenCalledWith(pointUse);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackClientById', () => {
        it('Should return tracked Client primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackClientById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackPointUsageConceptById', () => {
        it('Should return tracked PointUsageConcept primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPointUsageConceptById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
