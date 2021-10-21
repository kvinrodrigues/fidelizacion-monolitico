import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPointUse, getPointUseIdentifier } from '../point-use.model';

export type EntityResponseType = HttpResponse<IPointUse>;
export type EntityArrayResponseType = HttpResponse<IPointUse[]>;

@Injectable({ providedIn: 'root' })
export class PointUseService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/point-uses');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(pointUse: IPointUse): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pointUse);
    return this.http
      .post<IPointUse>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(pointUse: IPointUse): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pointUse);
    return this.http
      .put<IPointUse>(`${this.resourceUrl}/${getPointUseIdentifier(pointUse) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(pointUse: IPointUse): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pointUse);
    return this.http
      .patch<IPointUse>(`${this.resourceUrl}/${getPointUseIdentifier(pointUse) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPointUse>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPointUse[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPointUseToCollectionIfMissing(pointUseCollection: IPointUse[], ...pointUsesToCheck: (IPointUse | null | undefined)[]): IPointUse[] {
    const pointUses: IPointUse[] = pointUsesToCheck.filter(isPresent);
    if (pointUses.length > 0) {
      const pointUseCollectionIdentifiers = pointUseCollection.map(pointUseItem => getPointUseIdentifier(pointUseItem)!);
      const pointUsesToAdd = pointUses.filter(pointUseItem => {
        const pointUseIdentifier = getPointUseIdentifier(pointUseItem);
        if (pointUseIdentifier == null || pointUseCollectionIdentifiers.includes(pointUseIdentifier)) {
          return false;
        }
        pointUseCollectionIdentifiers.push(pointUseIdentifier);
        return true;
      });
      return [...pointUsesToAdd, ...pointUseCollection];
    }
    return pointUseCollection;
  }

  protected convertDateFromClient(pointUse: IPointUse): IPointUse {
    return Object.assign({}, pointUse, {
      eventDate: pointUse.eventDate?.isValid() ? pointUse.eventDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.eventDate = res.body.eventDate ? dayjs(res.body.eventDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((pointUse: IPointUse) => {
        pointUse.eventDate = pointUse.eventDate ? dayjs(pointUse.eventDate) : undefined;
      });
    }
    return res;
  }
}
