import { IMaterialList } from 'app/shared/model/box/material-list.model';
import { IBoxitem } from 'app/shared/model/box/boxitem.model';

export interface IMaterialListItem {
  id?: number;
  checkedIn?: boolean;
  comment?: string;
  materialList?: IMaterialList;
  boxitem?: IBoxitem;
}

export class MaterialListItem implements IMaterialListItem {
  constructor(
    public id?: number,
    public checkedIn?: boolean,
    public comment?: string,
    public materialList?: IMaterialList,
    public boxitem?: IBoxitem
  ) {
    this.checkedIn = this.checkedIn || false;
  }
}
