/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { MaterialListDetailComponent } from 'app/entities/box/material-list/material-list-detail.component';
import { MaterialList } from 'app/shared/model/box/material-list.model';

describe('Component Tests', () => {
  describe('MaterialList Management Detail Component', () => {
    let comp: MaterialListDetailComponent;
    let fixture: ComponentFixture<MaterialListDetailComponent>;
    const route = ({ data: of({ materialList: new MaterialList(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [MaterialListDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(MaterialListDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MaterialListDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.materialList).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
