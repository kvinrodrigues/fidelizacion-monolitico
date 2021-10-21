import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPointAllocationRule, getPointAllocationRuleIdentifier } from '../point-allocation-rule.model';

export type EntityResponseType = HttpResponse<IPointAllocationRule>;
export type EntityArrayResponseType = HttpResponse<IPointAllocationRule[]>;

@Injectable({ providedIn: 'root' })
export class PointAllocationRuleService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/point-allocation-rules');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(pointAllocationRule: IPointAllocationRule): Observable<EntityResponseType> {
    return this.http.post<IPointAllocationRule>(this.resourceUrl, pointAllocationRule, { observe: 'response' });
  }

  update(pointAllocationRule: IPointAllocationRule): Observable<EntityResponseType> {
    return this.http.put<IPointAllocationRule>(
      `${this.resourceUrl}/${getPointAllocationRuleIdentifier(pointAllocationRule) as number}`,
      pointAllocationRule,
      { observe: 'response' }
    );
  }

  partialUpdate(pointAllocationRule: IPointAllocationRule): Observable<EntityResponseType> {
    return this.http.patch<IPointAllocationRule>(
      `${this.resourceUrl}/${getPointAllocationRuleIdentifier(pointAllocationRule) as number}`,
      pointAllocationRule,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPointAllocationRule>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPointAllocationRule[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPointAllocationRuleToCollectionIfMissing(
    pointAllocationRuleCollection: IPointAllocationRule[],
    ...pointAllocationRulesToCheck: (IPointAllocationRule | null | undefined)[]
  ): IPointAllocationRule[] {
    const pointAllocationRules: IPointAllocationRule[] = pointAllocationRulesToCheck.filter(isPresent);
    if (pointAllocationRules.length > 0) {
      const pointAllocationRuleCollectionIdentifiers = pointAllocationRuleCollection.map(
        pointAllocationRuleItem => getPointAllocationRuleIdentifier(pointAllocationRuleItem)!
      );
      const pointAllocationRulesToAdd = pointAllocationRules.filter(pointAllocationRuleItem => {
        const pointAllocationRuleIdentifier = getPointAllocationRuleIdentifier(pointAllocationRuleItem);
        if (pointAllocationRuleIdentifier == null || pointAllocationRuleCollectionIdentifiers.includes(pointAllocationRuleIdentifier)) {
          return false;
        }
        pointAllocationRuleCollectionIdentifiers.push(pointAllocationRuleIdentifier);
        return true;
      });
      return [...pointAllocationRulesToAdd, ...pointAllocationRuleCollection];
    }
    return pointAllocationRuleCollection;
  }
}
