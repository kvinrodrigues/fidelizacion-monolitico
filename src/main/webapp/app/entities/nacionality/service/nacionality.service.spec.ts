import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { INacionality, Nacionality } from '../nacionality.model';

import { NacionalityService } from './nacionality.service';

describe('Nacionality Service', () => {
  let service: NacionalityService;
  let httpMock: HttpTestingController;
  let elemDefault: INacionality;
  let expectedResult: INacionality | INacionality[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(NacionalityService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      description: 'AAAAAAA',
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

    it('should create a Nacionality', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Nacionality()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Nacionality', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          description: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Nacionality', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
          description: 'BBBBBB',
        },
        new Nacionality()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Nacionality', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          description: 'BBBBBB',
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

    it('should delete a Nacionality', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addNacionalityToCollectionIfMissing', () => {
      it('should add a Nacionality to an empty array', () => {
        const nacionality: INacionality = { id: 123 };
        expectedResult = service.addNacionalityToCollectionIfMissing([], nacionality);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(nacionality);
      });

      it('should not add a Nacionality to an array that contains it', () => {
        const nacionality: INacionality = { id: 123 };
        const nacionalityCollection: INacionality[] = [
          {
            ...nacionality,
          },
          { id: 456 },
        ];
        expectedResult = service.addNacionalityToCollectionIfMissing(nacionalityCollection, nacionality);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Nacionality to an array that doesn't contain it", () => {
        const nacionality: INacionality = { id: 123 };
        const nacionalityCollection: INacionality[] = [{ id: 456 }];
        expectedResult = service.addNacionalityToCollectionIfMissing(nacionalityCollection, nacionality);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(nacionality);
      });

      it('should add only unique Nacionality to an array', () => {
        const nacionalityArray: INacionality[] = [{ id: 123 }, { id: 456 }, { id: 93528 }];
        const nacionalityCollection: INacionality[] = [{ id: 123 }];
        expectedResult = service.addNacionalityToCollectionIfMissing(nacionalityCollection, ...nacionalityArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const nacionality: INacionality = { id: 123 };
        const nacionality2: INacionality = { id: 456 };
        expectedResult = service.addNacionalityToCollectionIfMissing([], nacionality, nacionality2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(nacionality);
        expect(expectedResult).toContain(nacionality2);
      });

      it('should accept null and undefined values', () => {
        const nacionality: INacionality = { id: 123 };
        expectedResult = service.addNacionalityToCollectionIfMissing([], null, nacionality, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(nacionality);
      });

      it('should return initial array if no Nacionality is added', () => {
        const nacionalityCollection: INacionality[] = [{ id: 123 }];
        expectedResult = service.addNacionalityToCollectionIfMissing(nacionalityCollection, undefined, null);
        expect(expectedResult).toEqual(nacionalityCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
