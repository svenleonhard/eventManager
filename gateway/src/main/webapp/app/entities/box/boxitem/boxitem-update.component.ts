import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IBoxitem, Boxitem } from 'app/shared/model/box/boxitem.model';
import { BoxitemService } from './boxitem.service';
import { IBox } from 'app/shared/model/box/box.model';
import { BoxService } from 'app/entities/box/box';
import { IItem } from 'app/shared/model/box/item.model';
import { ItemService } from 'app/entities/box/item';

@Component({
  selector: 'jhi-boxitem-update',
  templateUrl: './boxitem-update.component.html'
})
export class BoxitemUpdateComponent implements OnInit {
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
    protected boxitemService: BoxitemService,
    protected boxService: BoxService,
    protected itemService: ItemService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ boxitem }) => {
      this.updateForm(boxitem);
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

  updateForm(boxitem: IBoxitem) {
    this.editForm.patchValue({
      id: boxitem.id,
      toRepair: boxitem.toRepair,
      comment: boxitem.comment,
      box: boxitem.box,
      item: boxitem.item
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const boxitem = this.createFromForm();
    if (boxitem.id !== undefined) {
      this.subscribeToSaveResponse(this.boxitemService.update(boxitem));
    } else {
      this.subscribeToSaveResponse(this.boxitemService.create(boxitem));
    }
  }

  private createFromForm(): IBoxitem {
    return {
      ...new Boxitem(),
      id: this.editForm.get(['id']).value,
      toRepair: this.editForm.get(['toRepair']).value,
      comment: this.editForm.get(['comment']).value,
      box: this.editForm.get(['box']).value,
      item: this.editForm.get(['item']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBoxitem>>) {
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
