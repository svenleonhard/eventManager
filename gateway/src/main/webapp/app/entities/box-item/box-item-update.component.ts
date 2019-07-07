import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IBoxItem, BoxItem } from 'app/shared/model/box-item.model';
import { BoxItemService } from './box-item.service';
import { IBox } from 'app/shared/model/box.model';
import { BoxService } from 'app/entities/box';
import { IItem } from 'app/shared/model/item.model';
import { ItemService } from 'app/entities/item';

@Component({
  selector: 'jhi-box-item-update',
  templateUrl: './box-item-update.component.html'
})
export class BoxItemUpdateComponent implements OnInit {
  isSaving: boolean;

  boxes: IBox[];

  items: IItem[];

  editForm = this.fb.group({
    id: [],
    toRepair: [],
    comment: [],
    box: [],
    item: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected boxItemService: BoxItemService,
    protected boxService: BoxService,
    protected itemService: ItemService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ boxItem }) => {
      this.updateForm(boxItem);
    });
    this.boxService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IBox[]>) => mayBeOk.ok),
        map((response: HttpResponse<IBox[]>) => response.body)
      )
      .subscribe((res: IBox[]) => (this.boxes = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.itemService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IItem[]>) => mayBeOk.ok),
        map((response: HttpResponse<IItem[]>) => response.body)
      )
      .subscribe((res: IItem[]) => (this.items = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(boxItem: IBoxItem) {
    this.editForm.patchValue({
      id: boxItem.id,
      toRepair: boxItem.toRepair,
      comment: boxItem.comment,
      box: boxItem.box,
      item: boxItem.item
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const boxItem = this.createFromForm();
    if (boxItem.id !== undefined) {
      this.subscribeToSaveResponse(this.boxItemService.update(boxItem));
    } else {
      this.subscribeToSaveResponse(this.boxItemService.create(boxItem));
    }
  }

  private createFromForm(): IBoxItem {
    return {
      ...new BoxItem(),
      id: this.editForm.get(['id']).value,
      toRepair: this.editForm.get(['toRepair']).value,
      comment: this.editForm.get(['comment']).value,
      box: this.editForm.get(['box']).value,
      item: this.editForm.get(['item']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBoxItem>>) {
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

  trackBoxById(index: number, item: IBox) {
    return item.id;
  }

  trackItemById(index: number, item: IItem) {
    return item.id;
  }
}
