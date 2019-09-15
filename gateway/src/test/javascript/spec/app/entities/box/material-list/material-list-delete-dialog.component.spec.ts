/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { GatewayTestModule } from '../../../../test.module';
import { MaterialListDeleteDialogComponent } from 'app/entities/box/material-list/material-list-delete-dialog.component';
import { MaterialListService } from 'app/entities/box/material-list/material-list.service';

describe('Component Tests', () => {
  describe('MaterialList Management Delete Component', () => {
    let comp: MaterialListDeleteDialogComponent;
    let fixture: ComponentFixture<MaterialListDeleteDialogComponent>;
    let service: MaterialListService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [MaterialListDeleteDialogComponent]
      })
        .overrideTemplate(MaterialListDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MaterialListDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MaterialListService);
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
