import * as dayjs from 'dayjs';
import { IClient } from 'app/entities/client/client.model';
import { IPointUseDet } from 'app/entities/point-use-det/point-use-det.model';
import { IPointUsageConcept } from 'app/entities/point-usage-concept/point-usage-concept.model';

export interface IPointUse {
  id?: number;
  scoreUsed?: number;
  eventDate?: dayjs.Dayjs;
  client?: IClient | null;
  pointUseDetails?: IPointUseDet[] | null;
  pointUsageConcept?: IPointUsageConcept | null;
}

export class PointUse implements IPointUse {
  constructor(
    public id?: number,
    public scoreUsed?: number,
    public eventDate?: dayjs.Dayjs,
    public client?: IClient | null,
    public pointUseDetails?: IPointUseDet[] | null,
    public pointUsageConcept?: IPointUsageConcept | null
  ) {}
}

export function getPointUseIdentifier(pointUse: IPointUse): number | undefined {
  return pointUse.id;
}
