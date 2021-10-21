export interface IPointAllocationRule {
  id?: number;
  lowerLimit?: number | null;
  upperLimit?: number | null;
  equivalenceOfAPoint?: number;
}

export class PointAllocationRule implements IPointAllocationRule {
  constructor(
    public id?: number,
    public lowerLimit?: number | null,
    public upperLimit?: number | null,
    public equivalenceOfAPoint?: number
  ) {}
}

export function getPointAllocationRuleIdentifier(pointAllocationRule: IPointAllocationRule): number | undefined {
  return pointAllocationRule.id;
}
