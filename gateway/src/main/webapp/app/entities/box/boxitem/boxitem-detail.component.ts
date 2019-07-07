import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBoxitem } from 'app/shared/model/box/boxitem.model';

@Component({
  selector: 'jhi-boxitem-detail',
  templateUrl: './boxitem-detail.component.html'
})
export class BoxitemDetailComponent implements OnInit {
  boxitem: IBoxitem;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ boxitem }) => {
      this.boxitem = boxitem;
    });
  }

  previousState() {
    window.history.back();
  }
}
