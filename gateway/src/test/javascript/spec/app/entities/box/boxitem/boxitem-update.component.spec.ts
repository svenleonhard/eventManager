/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { BoxitemUpdateComponent } from 'app/entities/box/boxitem/boxitem-update.component';
import { BoxitemService } from 'app/entities/box/boxitem/boxitem.service';
import { Boxitem } from 'app/shared/model/box/boxitem.model';

describe('Component Tests', () => {
  describe('Boxitem Management Update Component', () => {
    let comp: BoxitemUpdateComponent;
    let fixture: ComponentFixture<BoxitemUpdateComponent>;
    let service: BoxitemService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [BoxitemUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(BoxitemUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BoxitemUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(BoxitemService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Boxitem(123);
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
        const entity = new Boxitem();
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
