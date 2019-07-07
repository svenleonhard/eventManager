import { IUser } from 'app/core/user/user.model';

export interface IBox {
  id?: number;
  extenedId?: string;
  name?: string;
  category?: string;
  description?: string;
  users?: IUser[];
}

export class Box implements IBox {
  constructor(
    public id?: number,
    public extenedId?: string,
    public name?: string,
    public category?: string,
    public description?: string,
    public users?: IUser[]
  ) {}
}
