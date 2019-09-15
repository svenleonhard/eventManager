import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMaterialListItem } from 'app/shared/model/box/material-list-item.model';
import { MaterialListItemService } from './material-list-item.service';

@Component({
  selector: 'jhi-material-list-item-delete-dialog',
  templateUrl: './material-list-item-delete-dialog.component.html'
})
export class MaterialListItemDeleteDialogComponent {
  materialListItem: IMaterialListItem;

  constructor(
    protected materialListItemService: MaterialListItemService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.materialListItemService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'materialListItemListModification',
        content: 'Deleted an materialListItem'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-material-list-item-delete-popup',
  template: ''
})
export class MaterialListItemDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ materialListItem }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(MaterialListItemDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.materialListItem = materialListItem;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/material-list-item', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/material-list-item', { outlets: { popup: null } }]);
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
