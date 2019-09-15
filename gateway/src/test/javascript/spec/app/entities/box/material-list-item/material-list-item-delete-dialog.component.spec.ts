/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { GatewayTestModule } from '../../../../test.module';
import { MaterialListItemDeleteDialogComponent } from 'app/entities/box/material-list-item/material-list-item-delete-dialog.component';
import { MaterialListItemService } from 'app/entities/box/material-list-item/material-list-item.service';

describe('Component Tests', () => {
  describe('MaterialListItem Management Delete Component', () => {
    let comp: MaterialListItemDeleteDialogComponent;
    let fixture: ComponentFixture<MaterialListItemDeleteDialogComponent>;
    let service: MaterialListItemService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [MaterialListItemDeleteDialogComponent]
      })
        .overrideTemplate(MaterialListItemDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MaterialListItemDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MaterialListItemService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
