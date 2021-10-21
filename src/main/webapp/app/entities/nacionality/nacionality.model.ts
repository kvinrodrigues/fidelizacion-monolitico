import { IClient } from 'app/entities/client/client.model';

export interface INacionality {
  id?: number;
  name?: string;
  description?: string;
  clients?: IClient[] | null;
}

export class Nacionality implements INacionality {
  constructor(public id?: number, public name?: string, public description?: string, public clients?: IClient[] | null) {}
}

export function getNacionalityIdentifier(nacionality: INacionality): number | undefined {
  return nacionality.id;
}
