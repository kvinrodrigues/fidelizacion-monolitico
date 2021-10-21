import * as dayjs from 'dayjs';
import { IClient } from 'app/entities/client/client.model';
import { IPointUseDet } from 'app/entities/point-use-det/point-use-det.model';

export interface IBagOfPoint {
  id?: number;
  asignationDate?: dayjs.Dayjs;
  expirationDate?: dayjs.Dayjs;
  assignedScore?: number;
  scoreUsed?: number;
  scoreBalance?: number;
  operationAmount?: number;
  clients?: IClient[] | null;
  pointUseDetails?: IPointUseDet[] | null;
}

export class BagOfPoint implements IBagOfPoint {
  constructor(
    public id?: number,
    public asignationDate?: dayjs.Dayjs,
    public expirationDate?: dayjs.Dayjs,
    public assignedScore?: number,
    public scoreUsed?: number,
    public scoreBalance?: number,
    public operationAmount?: number,
    public clients?: IClient[] | null,
    public pointUseDetails?: IPointUseDet[] | null
  ) {}
}

export function getBagOfPointIdentifier(bagOfPoint: IBagOfPoint): number | undefined {
  return bagOfPoint.id;
}
