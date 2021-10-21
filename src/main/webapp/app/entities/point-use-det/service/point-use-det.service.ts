import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPointUseDet, getPointUseDetIdentifier } from '../point-use-det.model';

export type EntityResponseType = HttpResponse<IPointUseDet>;
export type EntityArrayResponseType = HttpResponse<IPointUseDet[]>;

@Injectable({ providedIn: 'root' })
export class PointUseDetService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/point-use-dets');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(pointUseDet: IPointUseDet): Observable<EntityResponseType> {
    return this.http.post<IPointUseDet>(this.resourceUrl, pointUseDet, { observe: 'response' });
  }

  update(pointUseDet: IPointUseDet): Observable<EntityResponseType> {
    return this.http.put<IPointUseDet>(`${this.resourceUrl}/${getPointUseDetIdentifier(pointUseDet) as number}`, pointUseDet, {
      observe: 'response',
    });
  }

  partialUpdate(pointUseDet: IPointUseDet): Observable<EntityResponseType> {
    return this.http.patch<IPointUseDet>(`${this.resourceUrl}/${getPointUseDetIdentifier(pointUseDet) as number}`, pointUseDet, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPointUseDet>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPointUseDet[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPointUseDetToCollectionIfMissing(
    pointUseDetCollection: IPointUseDet[],
    ...pointUseDetsToCheck: (IPointUseDet | null | undefined)[]
  ): IPointUseDet[] {
    const pointUseDets: IPointUseDet[] = pointUseDetsToCheck.filter(isPresent);
    if (pointUseDets.length > 0) {
      const pointUseDetCollectionIdentifiers = pointUseDetCollection.map(pointUseDetItem => getPointUseDetIdentifier(pointUseDetItem)!);
      const pointUseDetsToAdd = pointUseDets.filter(pointUseDetItem => {
        const pointUseDetIdentifier = getPointUseDetIdentifier(pointUseDetItem);
        if (pointUseDetIdentifier == null || pointUseDetCollectionIdentifiers.includes(pointUseDetIdentifier)) {
          return false;
        }
        pointUseDetCollectionIdentifiers.push(pointUseDetIdentifier);
        return true;
      });
      return [...pointUseDetsToAdd, ...pointUseDetCollection];
    }
    return pointUseDetCollection;
  }
}
