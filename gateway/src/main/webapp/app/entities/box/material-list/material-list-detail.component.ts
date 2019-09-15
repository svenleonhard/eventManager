import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMaterialList } from 'app/shared/model/box/material-list.model';

@Component({
  selector: 'jhi-material-list-detail',
  templateUrl: './material-list-detail.component.html'
})
export class MaterialListDetailComponent implements OnInit {
  materialList: IMaterialList;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ materialList }) => {
      this.materialList = materialList;
    });
  }

  previousState() {
    window.history.back();
  }
}
