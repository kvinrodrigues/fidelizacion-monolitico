import * as dayjs from 'dayjs';
import { IDocumentType } from 'app/entities/document-type/document-type.model';
import { INacionality } from 'app/entities/nacionality/nacionality.model';
import { IBagOfPoint } from 'app/entities/bag-of-point/bag-of-point.model';

export interface IClient {
  id?: number;
  name?: string;
  lastName?: string;
  documentNumber?: string;
  email?: string;
  phoneNumber?: string;
  birthDate?: dayjs.Dayjs;
  documentType?: IDocumentType | null;
  nacionality?: INacionality | null;
  bagOfPoint?: IBagOfPoint | null;
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
    public documentType?: IDocumentType | null,
    public nacionality?: INacionality | null,
    public bagOfPoint?: IBagOfPoint | null
  ) {}
}

export function getClientIdentifier(client: IClient): number | undefined {
  return client.id;
}
