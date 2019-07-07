/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { GatewayTestModule } from '../../../test.module';
import { BoxItemUpdateComponent } from 'app/entities/box-item/box-item-update.component';
import { BoxItemService } from 'app/entities/box-item/box-item.service';
import { BoxItem } from 'app/shared/model/box-item.model';

describe('Component Tests', () => {
  describe('BoxItem Management Update Component', () => {
    let comp: BoxItemUpdateComponent;
    let fixture: ComponentFixture<BoxItemUpdateComponent>;
    let service: BoxItemService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [BoxItemUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(BoxItemUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BoxItemUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(BoxItemService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new BoxItem(123);
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
        const entity = new BoxItem();
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
