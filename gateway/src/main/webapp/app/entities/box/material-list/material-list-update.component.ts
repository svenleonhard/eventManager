import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IMaterialList, MaterialList } from 'app/shared/model/box/material-list.model';
import { MaterialListService } from './material-list.service';

@Component({
  selector: 'jhi-material-list-update',
  templateUrl: './material-list-update.component.html'
})
export class MaterialListUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required, Validators.minLength(3)]],
    eventStart: [],
    eventEnd: [],
    location: [],
    open: []
  });

  constructor(protected materialListService: MaterialListService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ materialList }) => {
      this.updateForm(materialList);
    });
  }

  updateForm(materialList: IMaterialList) {
    this.editForm.patchValue({
      id: materialList.id,
      name: materialList.name,
      eventStart: materialList.eventStart,
      eventEnd: materialList.eventEnd,
      location: materialList.location,
      open: materialList.open
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const materialList = this.createFromForm();
    if (materialList.id !== undefined) {
      this.subscribeToSaveResponse(this.materialListService.update(materialList));
    } else {
      this.subscribeToSaveResponse(this.materialListService.create(materialList));
    }
  }

  private createFromForm(): IMaterialList {
    return {
      ...new MaterialList(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      eventStart: this.editForm.get(['eventStart']).value,
      eventEnd: this.editForm.get(['eventEnd']).value,
      location: this.editForm.get(['location']).value,
      open: this.editForm.get(['open']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMaterialList>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
