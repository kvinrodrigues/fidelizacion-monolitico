import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBagOfPoint, getBagOfPointIdentifier } from '../bag-of-point.model';

export type EntityResponseType = HttpResponse<IBagOfPoint>;
export type EntityArrayResponseType = HttpResponse<IBagOfPoint[]>;

@Injectable({ providedIn: 'root' })
export class BagOfPointService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/bag-of-points');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(bagOfPoint: IBagOfPoint): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bagOfPoint);
    return this.http
      .post<IBagOfPoint>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(bagOfPoint: IBagOfPoint): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bagOfPoint);
    return this.http
      .put<IBagOfPoint>(`${this.resourceUrl}/${getBagOfPointIdentifier(bagOfPoint) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(bagOfPoint: IBagOfPoint): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bagOfPoint);
    return this.http
      .patch<IBagOfPoint>(`${this.resourceUrl}/${getBagOfPointIdentifier(bagOfPoint) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IBagOfPoint>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IBagOfPoint[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBagOfPointToCollectionIfMissing(
    bagOfPointCollection: IBagOfPoint[],
    ...bagOfPointsToCheck: (IBagOfPoint | null | undefined)[]
  ): IBagOfPoint[] {
    const bagOfPoints: IBagOfPoint[] = bagOfPointsToCheck.filter(isPresent);
    if (bagOfPoints.length > 0) {
      const bagOfPointCollectionIdentifiers = bagOfPointCollection.map(bagOfPointItem => getBagOfPointIdentifier(bagOfPointItem)!);
      const bagOfPointsToAdd = bagOfPoints.filter(bagOfPointItem => {
        const bagOfPointIdentifier = getBagOfPointIdentifier(bagOfPointItem);
        if (bagOfPointIdentifier == null || bagOfPointCollectionIdentifiers.includes(bagOfPointIdentifier)) {
          return false;
        }
        bagOfPointCollectionIdentifiers.push(bagOfPointIdentifier);
        return true;
      });
      return [...bagOfPointsToAdd, ...bagOfPointCollection];
    }
    return bagOfPointCollection;
  }

  protected convertDateFromClient(bagOfPoint: IBagOfPoint): IBagOfPoint {
    return Object.assign({}, bagOfPoint, {
      asignationDate: bagOfPoint.asignationDate?.isValid() ? bagOfPoint.asignationDate.toJSON() : undefined,
      expirationDate: bagOfPoint.expirationDate?.isValid() ? bagOfPoint.expirationDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.asignationDate = res.body.asignationDate ? dayjs(res.body.asignationDate) : undefined;
      res.body.expirationDate = res.body.expirationDate ? dayjs(res.body.expirationDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((bagOfPoint: IBagOfPoint) => {
        bagOfPoint.asignationDate = bagOfPoint.asignationDate ? dayjs(bagOfPoint.asignationDate) : undefined;
        bagOfPoint.expirationDate = bagOfPoint.expirationDate ? dayjs(bagOfPoint.expirationDate) : undefined;
      });
    }
    return res;
  }
}
