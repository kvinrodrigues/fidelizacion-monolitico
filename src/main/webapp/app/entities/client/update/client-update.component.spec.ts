jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ClientService } from '../service/client.service';
import { IClient, Client } from '../client.model';
import { IDocumentType } from 'app/entities/document-type/document-type.model';
import { DocumentTypeService } from 'app/entities/document-type/service/document-type.service';
import { INacionality } from 'app/entities/nacionality/nacionality.model';
import { NacionalityService } from 'app/entities/nacionality/service/nacionality.service';
import { IBagOfPoint } from 'app/entities/bag-of-point/bag-of-point.model';
import { BagOfPointService } from 'app/entities/bag-of-point/service/bag-of-point.service';

import { ClientUpdateComponent } from './client-update.component';

describe('Component Tests', () => {
  describe('Client Management Update Component', () => {
    let comp: ClientUpdateComponent;
    let fixture: ComponentFixture<ClientUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let clientService: ClientService;
    let documentTypeService: DocumentTypeService;
    let nacionalityService: NacionalityService;
    let bagOfPointService: BagOfPointService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ClientUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ClientUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ClientUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      clientService = TestBed.inject(ClientService);
      documentTypeService = TestBed.inject(DocumentTypeService);
      nacionalityService = TestBed.inject(NacionalityService);
      bagOfPointService = TestBed.inject(BagOfPointService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call DocumentType query and add missing value', () => {
        const client: IClient = { id: 456 };
        const documentType: IDocumentType = { id: 23955 };
        client.documentType = documentType;

        const documentTypeCollection: IDocumentType[] = [{ id: 92159 }];
        jest.spyOn(documentTypeService, 'query').mockReturnValue(of(new HttpResponse({ body: documentTypeCollection })));
        const additionalDocumentTypes = [documentType];
        const expectedCollection: IDocumentType[] = [...additionalDocumentTypes, ...documentTypeCollection];
        jest.spyOn(documentTypeService, 'addDocumentTypeToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ client });
        comp.ngOnInit();

        expect(documentTypeService.query).toHaveBeenCalled();
        expect(documentTypeService.addDocumentTypeToCollectionIfMissing).toHaveBeenCalledWith(
          documentTypeCollection,
          ...additionalDocumentTypes
        );
        expect(comp.documentTypesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Nacionality query and add missing value', () => {
        const client: IClient = { id: 456 };
        const nacionality: INacionality = { id: 78071 };
        client.nacionality = nacionality;

        const nacionalityCollection: INacionality[] = [{ id: 18813 }];
        jest.spyOn(nacionalityService, 'query').mockReturnValue(of(new HttpResponse({ body: nacionalityCollection })));
        const additionalNacionalities = [nacionality];
        const expectedCollection: INacionality[] = [...additionalNacionalities, ...nacionalityCollection];
        jest.spyOn(nacionalityService, 'addNacionalityToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ client });
        comp.ngOnInit();

        expect(nacionalityService.query).toHaveBeenCalled();
        expect(nacionalityService.addNacionalityToCollectionIfMissing).toHaveBeenCalledWith(
          nacionalityCollection,
          ...additionalNacionalities
        );
        expect(comp.nacionalitiesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call BagOfPoint query and add missing value', () => {
        const client: IClient = { id: 456 };
        const bagOfPoint: IBagOfPoint = { id: 77056 };
        client.bagOfPoint = bagOfPoint;

        const bagOfPointCollection: IBagOfPoint[] = [{ id: 4097 }];
        jest.spyOn(bagOfPointService, 'query').mockReturnValue(of(new HttpResponse({ body: bagOfPointCollection })));
        const additionalBagOfPoints = [bagOfPoint];
        const expectedCollection: IBagOfPoint[] = [...additionalBagOfPoints, ...bagOfPointCollection];
        jest.spyOn(bagOfPointService, 'addBagOfPointToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ client });
        comp.ngOnInit();

        expect(bagOfPointService.query).toHaveBeenCalled();
        expect(bagOfPointService.addBagOfPointToCollectionIfMissing).toHaveBeenCalledWith(bagOfPointCollection, ...additionalBagOfPoints);
        expect(comp.bagOfPointsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const client: IClient = { id: 456 };
        const documentType: IDocumentType = { id: 8105 };
        client.documentType = documentType;
        const nacionality: INacionality = { id: 17236 };
        client.nacionality = nacionality;
        const bagOfPoint: IBagOfPoint = { id: 54269 };
        client.bagOfPoint = bagOfPoint;

        activatedRoute.data = of({ client });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(client));
        expect(comp.documentTypesSharedCollection).toContain(documentType);
        expect(comp.nacionalitiesSharedCollection).toContain(nacionality);
        expect(comp.bagOfPointsSharedCollection).toContain(bagOfPoint);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Client>>();
        const client = { id: 123 };
        jest.spyOn(clientService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ client });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: client }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(clientService.update).toHaveBeenCalledWith(client);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Client>>();
        const client = new Client();
        jest.spyOn(clientService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ client });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: client }));
        saveSubject.complete();

        // THEN
        expect(clientService.create).toHaveBeenCalledWith(client);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Client>>();
        const client = { id: 123 };
        jest.spyOn(clientService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ client });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(clientService.update).toHaveBeenCalledWith(client);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackDocumentTypeById', () => {
        it('Should return tracked DocumentType primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackDocumentTypeById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackNacionalityById', () => {
        it('Should return tracked Nacionality primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackNacionalityById(0, entity);
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
