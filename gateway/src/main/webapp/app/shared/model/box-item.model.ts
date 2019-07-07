import { IBox } from 'app/shared/model/box.model';
import { IItem } from 'app/shared/model/item.model';

export interface IBoxItem {
  id?: number;
  toRepair?: boolean;
  comment?: string;
  box?: IBox;
  item?: IItem;
}

export class BoxItem implements IBoxItem {
  constructor(public id?: number, public toRepair?: boolean, public comment?: string, public box?: IBox, public item?: IItem) {
    this.toRepair = this.toRepair || false;
  }
}
