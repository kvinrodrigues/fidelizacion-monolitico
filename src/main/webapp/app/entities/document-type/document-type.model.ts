import { IClient } from 'app/entities/client/client.model';

export interface IDocumentType {
  id?: number;
  name?: string;
  description?: string;
  clients?: IClient[] | null;
}

export class DocumentType implements IDocumentType {
  constructor(public id?: number, public name?: string, public description?: string, public clients?: IClient[] | null) {}
}

export function getDocumentTypeIdentifier(documentType: IDocumentType): number | undefined {
  return documentType.id;
}
