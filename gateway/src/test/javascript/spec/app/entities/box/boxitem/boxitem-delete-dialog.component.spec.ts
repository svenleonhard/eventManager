/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { GatewayTestModule } from '../../../../test.module';
import { BoxitemDeleteDialogComponent } from 'app/entities/box/boxitem/boxitem-delete-dialog.component';
import { BoxitemService } from 'app/entities/box/boxitem/boxitem.service';

describe('Component Tests', () => {
  describe('Boxitem Management Delete Component', () => {
    let comp: BoxitemDeleteDialogComponent;
    let fixture: ComponentFixture<BoxitemDeleteDialogComponent>;
    let service: BoxitemService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [BoxitemDeleteDialogComponent]
      })
        .overrideTemplate(BoxitemDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BoxitemDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(BoxitemService);
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
