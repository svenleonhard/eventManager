/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { MaterialListUpdateComponent } from 'app/entities/box/material-list/material-list-update.component';
import { MaterialListService } from 'app/entities/box/material-list/material-list.service';
import { MaterialList } from 'app/shared/model/box/material-list.model';

describe('Component Tests', () => {
  describe('MaterialList Management Update Component', () => {
    let comp: MaterialListUpdateComponent;
    let fixture: ComponentFixture<MaterialListUpdateComponent>;
    let service: MaterialListService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [MaterialListUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(MaterialListUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MaterialListUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MaterialListService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new MaterialList(123);
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
        const entity = new MaterialList();
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
