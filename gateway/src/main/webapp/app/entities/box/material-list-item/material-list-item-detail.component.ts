import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMaterialListItem } from 'app/shared/model/box/material-list-item.model';

@Component({
  selector: 'jhi-material-list-item-detail',
  templateUrl: './material-list-item-detail.component.html'
})
export class MaterialListItemDetailComponent implements OnInit {
  materialListItem: IMaterialListItem;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ materialListItem }) => {
      this.materialListItem = materialListItem;
    });
  }

  previousState() {
    window.history.back();
  }
}
