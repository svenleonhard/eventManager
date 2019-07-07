import { IBox } from 'app/shared/model/box/box.model';
import { IItem } from 'app/shared/model/box/item.model';

export interface IBoxitem {
  id?: number;
  toRepair?: boolean;
  comment?: string;
  box?: IBox;
  item?: IItem;
}

export class Boxitem implements IBoxitem {
  constructor(public id?: number, public toRepair?: boolean, public comment?: string, public box?: IBox, public item?: IItem) {
    this.toRepair = this.toRepair || false;
  }
}
