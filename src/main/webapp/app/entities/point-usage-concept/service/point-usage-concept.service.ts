import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPointUsageConcept, getPointUsageConceptIdentifier } from '../point-usage-concept.model';

export type EntityResponseType = HttpResponse<IPointUsageConcept>;
export type EntityArrayResponseType = HttpResponse<IPointUsageConcept[]>;

@Injectable({ providedIn: 'root' })
export class PointUsageConceptService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/point-usage-concepts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(pointUsageConcept: IPointUsageConcept): Observable<EntityResponseType> {
    return this.http.post<IPointUsageConcept>(this.resourceUrl, pointUsageConcept, { observe: 'response' });
  }

  update(pointUsageConcept: IPointUsageConcept): Observable<EntityResponseType> {
    return this.http.put<IPointUsageConcept>(
      `${this.resourceUrl}/${getPointUsageConceptIdentifier(pointUsageConcept) as number}`,
      pointUsageConcept,
      { observe: 'response' }
    );
  }

  partialUpdate(pointUsageConcept: IPointUsageConcept): Observable<EntityResponseType> {
    return this.http.patch<IPointUsageConcept>(
      `${this.resourceUrl}/${getPointUsageConceptIdentifier(pointUsageConcept) as number}`,
      pointUsageConcept,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPointUsageConcept>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPointUsageConcept[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPointUsageConceptToCollectionIfMissing(
    pointUsageConceptCollection: IPointUsageConcept[],
    ...pointUsageConceptsToCheck: (IPointUsageConcept | null | undefined)[]
  ): IPointUsageConcept[] {
    const pointUsageConcepts: IPointUsageConcept[] = pointUsageConceptsToCheck.filter(isPresent);
    if (pointUsageConcepts.length > 0) {
      const pointUsageConceptCollectionIdentifiers = pointUsageConceptCollection.map(
        pointUsageConceptItem => getPointUsageConceptIdentifier(pointUsageConceptItem)!
      );
      const pointUsageConceptsToAdd = pointUsageConcepts.filter(pointUsageConceptItem => {
        const pointUsageConceptIdentifier = getPointUsageConceptIdentifier(pointUsageConceptItem);
        if (pointUsageConceptIdentifier == null || pointUsageConceptCollectionIdentifiers.includes(pointUsageConceptIdentifier)) {
          return false;
        }
        pointUsageConceptCollectionIdentifiers.push(pointUsageConceptIdentifier);
        return true;
      });
      return [...pointUsageConceptsToAdd, ...pointUsageConceptCollection];
    }
    return pointUsageConceptCollection;
  }
}
