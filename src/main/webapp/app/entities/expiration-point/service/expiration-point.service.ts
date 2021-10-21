import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IExpirationPoint, getExpirationPointIdentifier } from '../expiration-point.model';

export type EntityResponseType = HttpResponse<IExpirationPoint>;
export type EntityArrayResponseType = HttpResponse<IExpirationPoint[]>;

@Injectable({ providedIn: 'root' })
export class ExpirationPointService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/expiration-points');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(expirationPoint: IExpirationPoint): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(expirationPoint);
    return this.http
      .post<IExpirationPoint>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(expirationPoint: IExpirationPoint): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(expirationPoint);
    return this.http
      .put<IExpirationPoint>(`${this.resourceUrl}/${getExpirationPointIdentifier(expirationPoint) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(expirationPoint: IExpirationPoint): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(expirationPoint);
    return this.http
      .patch<IExpirationPoint>(`${this.resourceUrl}/${getExpirationPointIdentifier(expirationPoint) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IExpirationPoint>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IExpirationPoint[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addExpirationPointToCollectionIfMissing(
    expirationPointCollection: IExpirationPoint[],
    ...expirationPointsToCheck: (IExpirationPoint | null | undefined)[]
  ): IExpirationPoint[] {
    const expirationPoints: IExpirationPoint[] = expirationPointsToCheck.filter(isPresent);
    if (expirationPoints.length > 0) {
      const expirationPointCollectionIdentifiers = expirationPointCollection.map(
        expirationPointItem => getExpirationPointIdentifier(expirationPointItem)!
      );
      const expirationPointsToAdd = expirationPoints.filter(expirationPointItem => {
        const expirationPointIdentifier = getExpirationPointIdentifier(expirationPointItem);
        if (expirationPointIdentifier == null || expirationPointCollectionIdentifiers.includes(expirationPointIdentifier)) {
          return false;
        }
        expirationPointCollectionIdentifiers.push(expirationPointIdentifier);
        return true;
      });
      return [...expirationPointsToAdd, ...expirationPointCollection];
    }
    return expirationPointCollection;
  }

  protected convertDateFromClient(expirationPoint: IExpirationPoint): IExpirationPoint {
    return Object.assign({}, expirationPoint, {
      validityStartDate: expirationPoint.validityStartDate?.isValid() ? expirationPoint.validityStartDate.toJSON() : undefined,
      validityEndDate: expirationPoint.validityEndDate?.isValid() ? expirationPoint.validityEndDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.validityStartDate = res.body.validityStartDate ? dayjs(res.body.validityStartDate) : undefined;
      res.body.validityEndDate = res.body.validityEndDate ? dayjs(res.body.validityEndDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((expirationPoint: IExpirationPoint) => {
        expirationPoint.validityStartDate = expirationPoint.validityStartDate ? dayjs(expirationPoint.validityStartDate) : undefined;
        expirationPoint.validityEndDate = expirationPoint.validityEndDate ? dayjs(expirationPoint.validityEndDate) : undefined;
      });
    }
    return res;
  }
}
