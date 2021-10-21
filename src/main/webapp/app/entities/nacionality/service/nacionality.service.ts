import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { INacionality, getNacionalityIdentifier } from '../nacionality.model';

export type EntityResponseType = HttpResponse<INacionality>;
export type EntityArrayResponseType = HttpResponse<INacionality[]>;

@Injectable({ providedIn: 'root' })
export class NacionalityService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/nacionalities');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(nacionality: INacionality): Observable<EntityResponseType> {
    return this.http.post<INacionality>(this.resourceUrl, nacionality, { observe: 'response' });
  }

  update(nacionality: INacionality): Observable<EntityResponseType> {
    return this.http.put<INacionality>(`${this.resourceUrl}/${getNacionalityIdentifier(nacionality) as number}`, nacionality, {
      observe: 'response',
    });
  }

  partialUpdate(nacionality: INacionality): Observable<EntityResponseType> {
    return this.http.patch<INacionality>(`${this.resourceUrl}/${getNacionalityIdentifier(nacionality) as number}`, nacionality, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<INacionality>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<INacionality[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addNacionalityToCollectionIfMissing(
    nacionalityCollection: INacionality[],
    ...nacionalitiesToCheck: (INacionality | null | undefined)[]
  ): INacionality[] {
    const nacionalities: INacionality[] = nacionalitiesToCheck.filter(isPresent);
    if (nacionalities.length > 0) {
      const nacionalityCollectionIdentifiers = nacionalityCollection.map(nacionalityItem => getNacionalityIdentifier(nacionalityItem)!);
      const nacionalitiesToAdd = nacionalities.filter(nacionalityItem => {
        const nacionalityIdentifier = getNacionalityIdentifier(nacionalityItem);
        if (nacionalityIdentifier == null || nacionalityCollectionIdentifiers.includes(nacionalityIdentifier)) {
          return false;
        }
        nacionalityCollectionIdentifiers.push(nacionalityIdentifier);
        return true;
      });
      return [...nacionalitiesToAdd, ...nacionalityCollection];
    }
    return nacionalityCollection;
  }
}
