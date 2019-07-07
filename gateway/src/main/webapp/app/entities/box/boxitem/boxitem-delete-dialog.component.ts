import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IBoxitem } from 'app/shared/model/box/boxitem.model';
import { BoxitemService } from './boxitem.service';

@Component({
  selector: 'jhi-boxitem-delete-dialog',
  templateUrl: './boxitem-delete-dialog.component.html'
})
export class BoxitemDeleteDialogComponent {
  boxitem: IBoxitem;

  constructor(protected boxitemService: BoxitemService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.boxitemService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'boxitemListModification',
        content: 'Deleted an boxitem'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-boxitem-delete-popup',
  template: ''
})
export class BoxitemDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ boxitem }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(BoxitemDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.boxitem = boxitem;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/boxitem', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/boxitem', { outlets: { popup: null } }]);
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
