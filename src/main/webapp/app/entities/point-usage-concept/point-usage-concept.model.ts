import { IPointUse } from 'app/entities/point-use/point-use.model';

export interface IPointUsageConcept {
  id?: number;
  description?: string | null;
  requiredPoints?: number | null;
  pointUses?: IPointUse[] | null;
}

export class PointUsageConcept implements IPointUsageConcept {
  constructor(
    public id?: number,
    public description?: string | null,
    public requiredPoints?: number | null,
    public pointUses?: IPointUse[] | null
  ) {}
}

export function getPointUsageConceptIdentifier(pointUsageConcept: IPointUsageConcept): number | undefined {
  return pointUsageConcept.id;
}
