import * as dayjs from 'dayjs';

export interface IExpirationPoint {
  id?: number;
  validityStartDate?: dayjs.Dayjs;
  validityEndDate?: dayjs.Dayjs;
  scoreDurationDays?: number;
}

export class ExpirationPoint implements IExpirationPoint {
  constructor(
    public id?: number,
    public validityStartDate?: dayjs.Dayjs,
    public validityEndDate?: dayjs.Dayjs,
    public scoreDurationDays?: number
  ) {}
}

export function getExpirationPointIdentifier(expirationPoint: IExpirationPoint): number | undefined {
  return expirationPoint.id;
}
