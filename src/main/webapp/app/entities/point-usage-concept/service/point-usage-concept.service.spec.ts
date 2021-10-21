import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPointUsageConcept, PointUsageConcept } from '../point-usage-concept.model';

import { PointUsageConceptService } from './point-usage-concept.service';

describe('PointUsageConcept Service', () => {
  let service: PointUsageConceptService;
  let httpMock: HttpTestingController;
  let elemDefault: IPointUsageConcept;
  let expectedResult: IPointUsageConcept | IPointUsageConcept[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PointUsageConceptService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      description: 'AAAAAAA',
      requiredPoints: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a PointUsageConcept', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new PointUsageConcept()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PointUsageConcept', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          description: 'BBBBBB',
          requiredPoints: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PointUsageConcept', () => {
      const patchObject = Object.assign({}, new PointUsageConcept());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PointUsageConcept', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          description: 'BBBBBB',
          requiredPoints: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a PointUsageConcept', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPointUsageConceptToCollectionIfMissing', () => {
      it('should add a PointUsageConcept to an empty array', () => {
        const pointUsageConcept: IPointUsageConcept = { id: 123 };
        expectedResult = service.addPointUsageConceptToCollectionIfMissing([], pointUsageConcept);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pointUsageConcept);
      });

      it('should not add a PointUsageConcept to an array that contains it', () => {
        const pointUsageConcept: IPointUsageConcept = { id: 123 };
        const pointUsageConceptCollection: IPointUsageConcept[] = [
          {
            ...pointUsageConcept,
          },
          { id: 456 },
        ];
        expectedResult = service.addPointUsageConceptToCollectionIfMissing(pointUsageConceptCollection, pointUsageConcept);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PointUsageConcept to an array that doesn't contain it", () => {
        const pointUsageConcept: IPointUsageConcept = { id: 123 };
        const pointUsageConceptCollection: IPointUsageConcept[] = [{ id: 456 }];
        expectedResult = service.addPointUsageConceptToCollectionIfMissing(pointUsageConceptCollection, pointUsageConcept);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pointUsageConcept);
      });

      it('should add only unique PointUsageConcept to an array', () => {
        const pointUsageConceptArray: IPointUsageConcept[] = [{ id: 123 }, { id: 456 }, { id: 25935 }];
        const pointUsageConceptCollection: IPointUsageConcept[] = [{ id: 123 }];
        expectedResult = service.addPointUsageConceptToCollectionIfMissing(pointUsageConceptCollection, ...pointUsageConceptArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pointUsageConcept: IPointUsageConcept = { id: 123 };
        const pointUsageConcept2: IPointUsageConcept = { id: 456 };
        expectedResult = service.addPointUsageConceptToCollectionIfMissing([], pointUsageConcept, pointUsageConcept2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pointUsageConcept);
        expect(expectedResult).toContain(pointUsageConcept2);
      });

      it('should accept null and undefined values', () => {
        const pointUsageConcept: IPointUsageConcept = { id: 123 };
        expectedResult = service.addPointUsageConceptToCollectionIfMissing([], null, pointUsageConcept, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pointUsageConcept);
      });

      it('should return initial array if no PointUsageConcept is added', () => {
        const pointUsageConceptCollection: IPointUsageConcept[] = [{ id: 123 }];
        expectedResult = service.addPointUsageConceptToCollectionIfMissing(pointUsageConceptCollection, undefined, null);
        expect(expectedResult).toEqual(pointUsageConceptCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
