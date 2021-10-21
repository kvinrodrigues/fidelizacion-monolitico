import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IBagOfPoint, BagOfPoint } from '../bag-of-point.model';

import { BagOfPointService } from './bag-of-point.service';

describe('BagOfPoint Service', () => {
  let service: BagOfPointService;
  let httpMock: HttpTestingController;
  let elemDefault: IBagOfPoint;
  let expectedResult: IBagOfPoint | IBagOfPoint[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BagOfPointService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      asignationDate: currentDate,
      expirationDate: currentDate,
      assignedScore: 0,
      scoreUsed: 0,
      scoreBalance: 0,
      operationAmount: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          asignationDate: currentDate.format(DATE_TIME_FORMAT),
          expirationDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a BagOfPoint', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          asignationDate: currentDate.format(DATE_TIME_FORMAT),
          expirationDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          asignationDate: currentDate,
          expirationDate: currentDate,
        },
        returnedFromService
      );

      service.create(new BagOfPoint()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BagOfPoint', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          asignationDate: currentDate.format(DATE_TIME_FORMAT),
          expirationDate: currentDate.format(DATE_TIME_FORMAT),
          assignedScore: 1,
          scoreUsed: 1,
          scoreBalance: 1,
          operationAmount: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          asignationDate: currentDate,
          expirationDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BagOfPoint', () => {
      const patchObject = Object.assign(
        {
          asignationDate: currentDate.format(DATE_TIME_FORMAT),
          expirationDate: currentDate.format(DATE_TIME_FORMAT),
          assignedScore: 1,
          scoreBalance: 1,
        },
        new BagOfPoint()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          asignationDate: currentDate,
          expirationDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BagOfPoint', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          asignationDate: currentDate.format(DATE_TIME_FORMAT),
          expirationDate: currentDate.format(DATE_TIME_FORMAT),
          assignedScore: 1,
          scoreUsed: 1,
          scoreBalance: 1,
          operationAmount: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          asignationDate: currentDate,
          expirationDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a BagOfPoint', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addBagOfPointToCollectionIfMissing', () => {
      it('should add a BagOfPoint to an empty array', () => {
        const bagOfPoint: IBagOfPoint = { id: 123 };
        expectedResult = service.addBagOfPointToCollectionIfMissing([], bagOfPoint);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bagOfPoint);
      });

      it('should not add a BagOfPoint to an array that contains it', () => {
        const bagOfPoint: IBagOfPoint = { id: 123 };
        const bagOfPointCollection: IBagOfPoint[] = [
          {
            ...bagOfPoint,
          },
          { id: 456 },
        ];
        expectedResult = service.addBagOfPointToCollectionIfMissing(bagOfPointCollection, bagOfPoint);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BagOfPoint to an array that doesn't contain it", () => {
        const bagOfPoint: IBagOfPoint = { id: 123 };
        const bagOfPointCollection: IBagOfPoint[] = [{ id: 456 }];
        expectedResult = service.addBagOfPointToCollectionIfMissing(bagOfPointCollection, bagOfPoint);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bagOfPoint);
      });

      it('should add only unique BagOfPoint to an array', () => {
        const bagOfPointArray: IBagOfPoint[] = [{ id: 123 }, { id: 456 }, { id: 73274 }];
        const bagOfPointCollection: IBagOfPoint[] = [{ id: 123 }];
        expectedResult = service.addBagOfPointToCollectionIfMissing(bagOfPointCollection, ...bagOfPointArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const bagOfPoint: IBagOfPoint = { id: 123 };
        const bagOfPoint2: IBagOfPoint = { id: 456 };
        expectedResult = service.addBagOfPointToCollectionIfMissing([], bagOfPoint, bagOfPoint2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(bagOfPoint);
        expect(expectedResult).toContain(bagOfPoint2);
      });

      it('should accept null and undefined values', () => {
        const bagOfPoint: IBagOfPoint = { id: 123 };
        expectedResult = service.addBagOfPointToCollectionIfMissing([], null, bagOfPoint, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(bagOfPoint);
      });

      it('should return initial array if no BagOfPoint is added', () => {
        const bagOfPointCollection: IBagOfPoint[] = [{ id: 123 }];
        expectedResult = service.addBagOfPointToCollectionIfMissing(bagOfPointCollection, undefined, null);
        expect(expectedResult).toEqual(bagOfPointCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
