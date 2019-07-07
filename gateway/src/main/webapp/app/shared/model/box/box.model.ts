export interface IBox {
  id?: number;
  extenedId?: string;
  name?: string;
  category?: string;
  description?: string;
}

export class Box implements IBox {
  constructor(public id?: number, public extenedId?: string, public name?: string, public category?: string, public description?: string) {}
}
