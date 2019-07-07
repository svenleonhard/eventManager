import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBoxItem } from 'app/shared/model/box-item.model';
import { BoxItemService } from './box-item.service';

@Component({
  selector: 'jhi-box-item-delete-dialog',
  templateUrl: './box-item-delete-dialog.component.html'
})
export class BoxItemDeleteDialogComponent {
  boxItem: IBoxItem;

  constructor(protected boxItemService: BoxItemService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.boxItemService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'boxItemListModification',
        content: 'Deleted an boxItem'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-box-item-delete-popup',
  template: ''
})
export class BoxItemDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ boxItem }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(BoxItemDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.boxItem = boxItem;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/box-item', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/box-item', { outlets: { popup: null } }]);
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
