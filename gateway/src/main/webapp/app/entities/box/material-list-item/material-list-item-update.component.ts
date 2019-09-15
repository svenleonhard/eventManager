import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IMaterialListItem, MaterialListItem } from 'app/shared/model/box/material-list-item.model';
import { MaterialListItemService } from './material-list-item.service';
import { IMaterialList } from 'app/shared/model/box/material-list.model';
import { MaterialListService } from 'app/entities/box/material-list';
import { IBoxitem } from 'app/shared/model/box/boxitem.model';
import { BoxitemService } from 'app/entities/box/boxitem';

@Component({
  selector: 'jhi-material-list-item-update',
  templateUrl: './material-list-item-update.component.html'
})
export class MaterialListItemUpdateComponent implements OnInit {
  isSaving: boolean;

  materiallists: IMaterialList[];

  boxitems: IBoxitem[];

  editForm = this.fb.group({
    id: [],
    checkedIn: [],
    comment: [],
    materialList: [],
    boxitem: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected materialListItemService: MaterialListItemService,
    protected materialListService: MaterialListService,
    protected boxitemService: BoxitemService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ materialListItem }) => {
      this.updateForm(materialListItem);
    });
    this.materialListService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IMaterialList[]>) => mayBeOk.ok),
        map((response: HttpResponse<IMaterialList[]>) => response.body)
      )
      .subscribe((res: IMaterialList[]) => (this.materiallists = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.boxitemService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IBoxitem[]>) => mayBeOk.ok),
        map((response: HttpResponse<IBoxitem[]>) => response.body)
      )
      .subscribe((res: IBoxitem[]) => (this.boxitems = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(materialListItem: IMaterialListItem) {
    this.editForm.patchValue({
      id: materialListItem.id,
      checkedIn: materialListItem.checkedIn,
      comment: materialListItem.comment,
      materialList: materialListItem.materialList,
      boxitem: materialListItem.boxitem
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const materialListItem = this.createFromForm();
    if (materialListItem.id !== undefined) {
      this.subscribeToSaveResponse(this.materialListItemService.update(materialListItem));
    } else {
      this.subscribeToSaveResponse(this.materialListItemService.create(materialListItem));
    }
  }

  private createFromForm(): IMaterialListItem {
    return {
      ...new MaterialListItem(),
      id: this.editForm.get(['id']).value,
      checkedIn: this.editForm.get(['checkedIn']).value,
      comment: this.editForm.get(['comment']).value,
      materialList: this.editForm.get(['materialList']).value,
      boxitem: this.editForm.get(['boxitem']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMaterialListItem>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackMaterialListById(index: number, item: IMaterialList) {
    return item.id;
  }

  trackBoxitemById(index: number, item: IBoxitem) {
    return item.id;
  }
}
