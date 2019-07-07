/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { GatewayTestModule } from '../../../test.module';
import { BoxUpdateComponent } from 'app/entities/box/box-update.component';
import { BoxService } from 'app/entities/box/box.service';
import { Box } from 'app/shared/model/box.model';

describe('Component Tests', () => {
  describe('Box Management Update Component', () => {
    let comp: BoxUpdateComponent;
    let fixture: ComponentFixture<BoxUpdateComponent>;
    let service: BoxService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [BoxUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(BoxUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BoxUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(BoxService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Box(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Box();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
