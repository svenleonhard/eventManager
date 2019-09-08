import { Component, OnInit } from '@angular/core';
import { BoxitemService } from 'app/entities/box/boxitem';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { IBoxitem } from 'app/shared/model/box/boxitem.model';
import { ITEMS_PER_PAGE } from 'app/shared';

@Component({
  selector: 'jhi-finder',
  templateUrl: './finder.component.html',
  styleUrls: ['./finder.component.scss']
})
export class FinderComponent implements OnInit {
  qrResultString: string;
  idInput: string;
  itemsPerPage: number;
  boxitems: any;
  page: any;

  constructor(protected boxitemService: BoxitemService) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.boxitems = [
      {
        item: {
          description: 'Test Item'
        },
        toRepair: true
      }
    ];
    this.idInput = '';
  }

  ngOnInit() {}

  onCodeResult(resultString) {
    this.boxitemService
      .query({
        'boxId.equals': resultString
      })
      .subscribe((res: HttpResponse<IBoxitem[]>) => console.log(res.body), (res: HttpErrorResponse) => console.log(res.message));

    console.log(this.boxitemService.find(resultString));
    this.qrResultString = resultString;
  }

  onIdSubmit() {
    console.log('ID: ' + this.idInput);
    this.boxitemService
      .query({
        'boxId.equals': this.idInput
      })
      .subscribe((res: HttpResponse<IBoxitem[]>) => (this.boxitems = res.body), (res: HttpErrorResponse) => console.log(res.message));
  }

  /*clearResult(): void {
    this.qrResultString = null;
  }*/
}
