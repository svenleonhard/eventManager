export interface IMaterialList {
  id?: number;
  name?: string;
  eventStart?: number;
  eventEnd?: number;
  location?: string;
  open?: boolean;
}

export class MaterialList implements IMaterialList {
  constructor(
    public id?: number,
    public name?: string,
    public eventStart?: number,
    public eventEnd?: number,
    public location?: string,
    public open?: boolean
  ) {
    this.open = this.open || false;
  }
}
