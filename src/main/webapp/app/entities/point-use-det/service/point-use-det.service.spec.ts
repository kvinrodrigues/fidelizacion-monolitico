import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPointUseDet, PointUseDet } from '../point-use-det.model';

import { PointUseDetService } from './point-use-det.service';

describe('PointUseDet Service', () => {
  let service: PointUseDetService;
  let httpMock: HttpTestingController;
  let elemDefault: IPointUseDet;
  let expectedResult: IPointUseDet | IPointUseDet[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PointUseDetService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      scoreUsed: 0,
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

    it('should create a PointUseDet', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new PointUseDet()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PointUseDet', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          scoreUsed: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PointUseDet', () => {
      const patchObject = Object.assign(
        {
          scoreUsed: 1,
        },
        new PointUseDet()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PointUseDet', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          scoreUsed: 1,
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

    it('should delete a PointUseDet', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addPointUseDetToCollectionIfMissing', () => {
      it('should add a PointUseDet to an empty array', () => {
        const pointUseDet: IPointUseDet = { id: 123 };
        expectedResult = service.addPointUseDetToCollectionIfMissing([], pointUseDet);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pointUseDet);
      });

      it('should not add a PointUseDet to an array that contains it', () => {
        const pointUseDet: IPointUseDet = { id: 123 };
        const pointUseDetCollection: IPointUseDet[] = [
          {
            ...pointUseDet,
          },
          { id: 456 },
        ];
        expectedResult = service.addPointUseDetToCollectionIfMissing(pointUseDetCollection, pointUseDet);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PointUseDet to an array that doesn't contain it", () => {
        const pointUseDet: IPointUseDet = { id: 123 };
        const pointUseDetCollection: IPointUseDet[] = [{ id: 456 }];
        expectedResult = service.addPointUseDetToCollectionIfMissing(pointUseDetCollection, pointUseDet);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pointUseDet);
      });

      it('should add only unique PointUseDet to an array', () => {
        const pointUseDetArray: IPointUseDet[] = [{ id: 123 }, { id: 456 }, { id: 25627 }];
        const pointUseDetCollection: IPointUseDet[] = [{ id: 123 }];
        expectedResult = service.addPointUseDetToCollectionIfMissing(pointUseDetCollection, ...pointUseDetArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const pointUseDet: IPointUseDet = { id: 123 };
        const pointUseDet2: IPointUseDet = { id: 456 };
        expectedResult = service.addPointUseDetToCollectionIfMissing([], pointUseDet, pointUseDet2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(pointUseDet);
        expect(expectedResult).toContain(pointUseDet2);
      });

      it('should accept null and undefined values', () => {
        const pointUseDet: IPointUseDet = { id: 123 };
        expectedResult = service.addPointUseDetToCollectionIfMissing([], null, pointUseDet, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(pointUseDet);
      });

      it('should return initial array if no PointUseDet is added', () => {
        const pointUseDetCollection: IPointUseDet[] = [{ id: 123 }];
        expectedResult = service.addPointUseDetToCollectionIfMissing(pointUseDetCollection, undefined, null);
        expect(expectedResult).toEqual(pointUseDetCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
