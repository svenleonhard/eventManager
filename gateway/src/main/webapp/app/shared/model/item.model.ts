export interface IItem {
  id?: number;
  name?: string;
  category?: string;
  description?: string;
  manufactor?: string;
}

export class Item implements IItem {
  constructor(
    public id?: number,
    public name?: string,
    public category?: string,
    public description?: string,
    public manufactor?: string
  ) {}
}
