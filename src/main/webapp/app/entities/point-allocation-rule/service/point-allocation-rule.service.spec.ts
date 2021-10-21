import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPointAllocationRule, PointAllocationRule } from '../point-allocation-rule.model';

import { PointAllocationRuleService } from './point-allocation-rule.service';

describe('PointAllocationRule Service', () => {
  let service: PointAllocationRuleService;
  let httpMock: HttpTestingController;
  let elemDefault: IPointAllocationRule;
  let expectedResult: IPointAllocationRule | IPointAllocationRule[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PointAllocationRuleService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      lowerLimit: 0,
      upperLimit: 0,
      equivalenceOfAPoint: 0,
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

    it('should create a PointAllocationRule', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new PointAllocationRule()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PointAllocationRule', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          lowerLimit: 1,
          upperLimit: 1,
          equivalenceOfAPoint: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PointAllocationRule', () => {
      const patchObject = Object.assign(
        {
          equivalenceOfAPoint: 1,
        },
        new PointAllocationRule()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PointAllocationRule', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          lowerLimit: 1,
          upperLimit: 1,
          equivalenceOfAPoint: 1,
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

    it('should delete a PointAllocationRule', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPointAllocationRuleToCollectionIfMissing', () => {
      it('should add a PointAllocationRule to an empty array', () => {
        const pointAllocationRule: IPointAllocationRule = { id: 123 };
        expectedResult = service.addPointAllocationRuleToCollectionIfMissing([], pointAllocationRule);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pointAllocationRule);
      });

      it('should not add a PointAllocationRule to an array that contains it', () => {
        const pointAllocationRule: IPointAllocationRule = { id: 123 };
        const pointAllocationRuleCollection: IPointAllocationRule[] = [
          {
            ...pointAllocationRule,
          },
          { id: 456 },
        ];
        expectedResult = service.addPointAllocationRuleToCollectionIfMissing(pointAllocationRuleCollection, pointAllocationRule);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PointAllocationRule to an array that doesn't contain it", () => {
        const pointAllocationRule: IPointAllocationRule = { id: 123 };
        const pointAllocationRuleCollection: IPointAllocationRule[] = [{ id: 456 }];
        expectedResult = service.addPointAllocationRuleToCollectionIfMissing(pointAllocationRuleCollection, pointAllocationRule);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pointAllocationRule);
      });

      it('should add only unique PointAllocationRule to an array', () => {
        const pointAllocationRuleArray: IPointAllocationRule[] = [{ id: 123 }, { id: 456 }, { id: 78339 }];
        const pointAllocationRuleCollection: IPointAllocationRule[] = [{ id: 123 }];
        expectedResult = service.addPointAllocationRuleToCollectionIfMissing(pointAllocationRuleCollection, ...pointAllocationRuleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pointAllocationRule: IPointAllocationRule = { id: 123 };
        const pointAllocationRule2: IPointAllocationRule = { id: 456 };
        expectedResult = service.addPointAllocationRuleToCollectionIfMissing([], pointAllocationRule, pointAllocationRule2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pointAllocationRule);
        expect(expectedResult).toContain(pointAllocationRule2);
      });

      it('should accept null and undefined values', () => {
        const pointAllocationRule: IPointAllocationRule = { id: 123 };
        expectedResult = service.addPointAllocationRuleToCollectionIfMissing([], null, pointAllocationRule, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pointAllocationRule);
      });

      it('should return initial array if no PointAllocationRule is added', () => {
        const pointAllocationRuleCollection: IPointAllocationRule[] = [{ id: 123 }];
        expectedResult = service.addPointAllocationRuleToCollectionIfMissing(pointAllocationRuleCollection, undefined, null);
        expect(expectedResult).toEqual(pointAllocationRuleCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
