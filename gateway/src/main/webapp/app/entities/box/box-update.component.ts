import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IBox, Box } from 'app/shared/model/box.model';
import { BoxService } from './box.service';
import { IUser, UserService } from 'app/core';

@Component({
  selector: 'jhi-box-update',
  templateUrl: './box-update.component.html'
})
export class BoxUpdateComponent implements OnInit {
  isSaving: boolean;

  users: IUser[];

  editForm = this.fb.group({
    id: [],
    extenedId: [],
    name: [null, [Validators.required, Validators.minLength(3)]],
    category: [],
    description: [],
    users: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected boxService: BoxService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ box }) => {
      this.updateForm(box);
    });
    this.userService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUser[]>) => response.body)
      )
      .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(box: IBox) {
    this.editForm.patchValue({
      id: box.id,
      extenedId: box.extenedId,
      name: box.name,
      category: box.category,
      description: box.description,
      users: box.users
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const box = this.createFromForm();
    if (box.id !== undefined) {
      this.subscribeToSaveResponse(this.boxService.update(box));
    } else {
      this.subscribeToSaveResponse(this.boxService.create(box));
    }
  }

  private createFromForm(): IBox {
    return {
      ...new Box(),
      id: this.editForm.get(['id']).value,
      extenedId: this.editForm.get(['extenedId']).value,
      name: this.editForm.get(['name']).value,
      category: this.editForm.get(['category']).value,
      description: this.editForm.get(['description']).value,
      users: this.editForm.get(['users']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBox>>) {
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

  trackUserById(index: number, item: IUser) {
    return item.id;
  }

  getSelected(selectedVals: Array<any>, option: any) {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
