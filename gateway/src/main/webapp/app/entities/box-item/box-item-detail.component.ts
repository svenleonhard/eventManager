import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBoxItem } from 'app/shared/model/box-item.model';

@Component({
  selector: 'jhi-box-item-detail',
  templateUrl: './box-item-detail.component.html'
})
export class BoxItemDetailComponent implements OnInit {
  boxItem: IBoxItem;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ boxItem }) => {
      this.boxItem = boxItem;
    });
  }

  previousState() {
    window.history.back();
  }
}
