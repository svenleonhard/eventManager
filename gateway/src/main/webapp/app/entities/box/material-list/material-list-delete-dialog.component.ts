import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMaterialList } from 'app/shared/model/box/material-list.model';
import { MaterialListService } from './material-list.service';

@Component({
  selector: 'jhi-material-list-delete-dialog',
  templateUrl: './material-list-delete-dialog.component.html'
})
export class MaterialListDeleteDialogComponent {
  materialList: IMaterialList;

  constructor(
    protected materialListService: MaterialListService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.materialListService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'materialListListModification',
        content: 'Deleted an materialList'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-material-list-delete-popup',
  template: ''
})
export class MaterialListDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ materialList }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(MaterialListDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.materialList = materialList;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/material-list', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/material-list', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
