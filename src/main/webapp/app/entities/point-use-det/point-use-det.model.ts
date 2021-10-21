import { IPointUse } from 'app/entities/point-use/point-use.model';
import { IBagOfPoint } from 'app/entities/bag-of-point/bag-of-point.model';

export interface IPointUseDet {
  id?: number;
  scoreUsed?: number;
  pointUse?: IPointUse | null;
  bagOfPoint?: IBagOfPoint | null;
}

export class PointUseDet implements IPointUseDet {
  constructor(public id?: number, public scoreUsed?: number, public pointUse?: IPointUse | null, public bagOfPoint?: IBagOfPoint | null) {}
}

export function getPointUseDetIdentifier(pointUseDet: IPointUseDet): number | undefined {
  return pointUseDet.id;
}
