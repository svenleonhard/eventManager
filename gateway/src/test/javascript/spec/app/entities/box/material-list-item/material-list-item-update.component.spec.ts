/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { MaterialListItemUpdateComponent } from 'app/entities/box/material-list-item/material-list-item-update.component';
import { MaterialListItemService } from 'app/entities/box/material-list-item/material-list-item.service';
import { MaterialListItem } from 'app/shared/model/box/material-list-item.model';

describe('Component Tests', () => {
  describe('MaterialListItem Management Update Component', () => {
    let comp: MaterialListItemUpdateComponent;
    let fixture: ComponentFixture<MaterialListItemUpdateComponent>;
    let service: MaterialListItemService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [MaterialListItemUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(MaterialListItemUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MaterialListItemUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MaterialListItemService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new MaterialListItem(123);
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
        const entity = new MaterialListItem();
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
