import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IPointUse, PointUse } from '../point-use.model';

import { PointUseService } from './point-use.service';

describe('PointUse Service', () => {
  let service: PointUseService;
  let httpMock: HttpTestingController;
  let elemDefault: IPointUse;
  let expectedResult: IPointUse | IPointUse[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PointUseService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      scoreUsed: 0,
      eventDate: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          eventDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a PointUse', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          eventDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          eventDate: currentDate,
        },
        returnedFromService
      );

      service.create(new PointUse()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PointUse', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          scoreUsed: 1,
          eventDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          eventDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PointUse', () => {
      const patchObject = Object.assign(
        {
          scoreUsed: 1,
        },
        new PointUse()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          eventDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PointUse', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          scoreUsed: 1,
          eventDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          eventDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a PointUse', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPointUseToCollectionIfMissing', () => {
      it('should add a PointUse to an empty array', () => {
        const pointUse: IPointUse = { id: 123 };
        expectedResult = service.addPointUseToCollectionIfMissing([], pointUse);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pointUse);
      });

      it('should not add a PointUse to an array that contains it', () => {
        const pointUse: IPointUse = { id: 123 };
        const pointUseCollection: IPointUse[] = [
          {
            ...pointUse,
          },
          { id: 456 },
        ];
        expectedResult = service.addPointUseToCollectionIfMissing(pointUseCollection, pointUse);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PointUse to an array that doesn't contain it", () => {
        const pointUse: IPointUse = { id: 123 };
        const pointUseCollection: IPointUse[] = [{ id: 456 }];
        expectedResult = service.addPointUseToCollectionIfMissing(pointUseCollection, pointUse);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pointUse);
      });

      it('should add only unique PointUse to an array', () => {
        const pointUseArray: IPointUse[] = [{ id: 123 }, { id: 456 }, { id: 42802 }];
        const pointUseCollection: IPointUse[] = [{ id: 123 }];
        expectedResult = service.addPointUseToCollectionIfMissing(pointUseCollection, ...pointUseArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pointUse: IPointUse = { id: 123 };
        const pointUse2: IPointUse = { id: 456 };
        expectedResult = service.addPointUseToCollectionIfMissing([], pointUse, pointUse2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pointUse);
        expect(expectedResult).toContain(pointUse2);
      });

      it('should accept null and undefined values', () => {
        const pointUse: IPointUse = { id: 123 };
        expectedResult = service.addPointUseToCollectionIfMissing([], null, pointUse, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pointUse);
      });

      it('should return initial array if no PointUse is added', () => {
        const pointUseCollection: IPointUse[] = [{ id: 123 }];
        expectedResult = service.addPointUseToCollectionIfMissing(pointUseCollection, undefined, null);
        expect(expectedResult).toEqual(pointUseCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
