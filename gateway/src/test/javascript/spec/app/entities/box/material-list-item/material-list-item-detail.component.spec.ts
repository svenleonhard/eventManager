/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { MaterialListItemDetailComponent } from 'app/entities/box/material-list-item/material-list-item-detail.component';
import { MaterialListItem } from 'app/shared/model/box/material-list-item.model';

describe('Component Tests', () => {
  describe('MaterialListItem Management Detail Component', () => {
    let comp: MaterialListItemDetailComponent;
    let fixture: ComponentFixture<MaterialListItemDetailComponent>;
    const route = ({ data: of({ materialListItem: new MaterialListItem(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [MaterialListItemDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(MaterialListItemDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MaterialListItemDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.materialListItem).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
