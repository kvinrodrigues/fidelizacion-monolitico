import * as dayjs from 'dayjs';
import { IBagOfPoint } from 'app/entities/bag-of-point/bag-of-point.model';
import { IPointUse } from 'app/entities/point-use/point-use.model';
import { IDocumentType } from 'app/entities/document-type/document-type.model';
import { INacionality } from 'app/entities/nacionality/nacionality.model';

export interface IClient {
  id?: number;
  name?: string;
  lastName?: string;
  documentNumber?: string;
  email?: string;
  phoneNumber?: string;
  birthDate?: dayjs.Dayjs;
  bagOfPoints?: IBagOfPoint[] | null;
  pointUses?: IPointUse[] | null;
  documentType?: IDocumentType | null;
  nacionality?: INacionality | null;
}

export class Client implements IClient {
  constructor(
    public id?: number,
    public name?: string,
    public lastName?: string,
    public documentNumber?: string,
    public email?: string,
    public phoneNumber?: string,
    public birthDate?: dayjs.Dayjs,
    public bagOfPoints?: IBagOfPoint[] | null,
    public pointUses?: IPointUse[] | null,
    public documentType?: IDocumentType | null,
    public nacionality?: INacionality | null
  ) {}
}

export function getClientIdentifier(client: IClient): number | undefined {
  return client.id;
}
