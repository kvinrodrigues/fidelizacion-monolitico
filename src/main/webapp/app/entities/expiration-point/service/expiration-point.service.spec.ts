import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IExpirationPoint, ExpirationPoint } from '../expiration-point.model';

import { ExpirationPointService } from './expiration-point.service';

describe('ExpirationPoint Service', () => {
  let service: ExpirationPointService;
  let httpMock: HttpTestingController;
  let elemDefault: IExpirationPoint;
  let expectedResult: IExpirationPoint | IExpirationPoint[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ExpirationPointService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      validityStartDate: currentDate,
      validityEndDate: currentDate,
      scoreDurationDays: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          validityStartDate: currentDate.format(DATE_TIME_FORMAT),
          validityEndDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a ExpirationPoint', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          validityStartDate: currentDate.format(DATE_TIME_FORMAT),
          validityEndDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          validityStartDate: currentDate,
          validityEndDate: currentDate,
        },
        returnedFromService
      );

      service.create(new ExpirationPoint()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ExpirationPoint', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          validityStartDate: currentDate.format(DATE_TIME_FORMAT),
          validityEndDate: currentDate.format(DATE_TIME_FORMAT),
          scoreDurationDays: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          validityStartDate: currentDate,
          validityEndDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ExpirationPoint', () => {
      const patchObject = Object.assign(
        {
          validityEndDate: currentDate.format(DATE_TIME_FORMAT),
        },
        new ExpirationPoint()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          validityStartDate: currentDate,
          validityEndDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ExpirationPoint', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          validityStartDate: currentDate.format(DATE_TIME_FORMAT),
          validityEndDate: currentDate.format(DATE_TIME_FORMAT),
          scoreDurationDays: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          validityStartDate: currentDate,
          validityEndDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a ExpirationPoint', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addExpirationPointToCollectionIfMissing', () => {
      it('should add a ExpirationPoint to an empty array', () => {
        const expirationPoint: IExpirationPoint = { id: 123 };
        expectedResult = service.addExpirationPointToCollectionIfMissing([], expirationPoint);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(expirationPoint);
      });

      it('should not add a ExpirationPoint to an array that contains it', () => {
        const expirationPoint: IExpirationPoint = { id: 123 };
        const expirationPointCollection: IExpirationPoint[] = [
          {
            ...expirationPoint,
          },
          { id: 456 },
        ];
        expectedResult = service.addExpirationPointToCollectionIfMissing(expirationPointCollection, expirationPoint);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ExpirationPoint to an array that doesn't contain it", () => {
        const expirationPoint: IExpirationPoint = { id: 123 };
        const expirationPointCollection: IExpirationPoint[] = [{ id: 456 }];
        expectedResult = service.addExpirationPointToCollectionIfMissing(expirationPointCollection, expirationPoint);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(expirationPoint);
      });

      it('should add only unique ExpirationPoint to an array', () => {
        const expirationPointArray: IExpirationPoint[] = [{ id: 123 }, { id: 456 }, { id: 88589 }];
        const expirationPointCollection: IExpirationPoint[] = [{ id: 123 }];
        expectedResult = service.addExpirationPointToCollectionIfMissing(expirationPointCollection, ...expirationPointArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const expirationPoint: IExpirationPoint = { id: 123 };
        const expirationPoint2: IExpirationPoint = { id: 456 };
        expectedResult = service.addExpirationPointToCollectionIfMissing([], expirationPoint, expirationPoint2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(expirationPoint);
        expect(expectedResult).toContain(expirationPoint2);
      });

      it('should accept null and undefined values', () => {
        const expirationPoint: IExpirationPoint = { id: 123 };
        expectedResult = service.addExpirationPointToCollectionIfMissing([], null, expirationPoint, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(expirationPoint);
      });

      it('should return initial array if no ExpirationPoint is added', () => {
        const expirationPointCollection: IExpirationPoint[] = [{ id: 123 }];
        expectedResult = service.addExpirationPointToCollectionIfMissing(expirationPointCollection, undefined, null);
        expect(expectedResult).toEqual(expirationPointCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
