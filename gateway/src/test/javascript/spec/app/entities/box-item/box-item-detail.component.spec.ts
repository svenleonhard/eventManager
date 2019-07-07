/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../test.module';
import { BoxItemDetailComponent } from 'app/entities/box-item/box-item-detail.component';
import { BoxItem } from 'app/shared/model/box-item.model';

describe('Component Tests', () => {
  describe('BoxItem Management Detail Component', () => {
    let comp: BoxItemDetailComponent;
    let fixture: ComponentFixture<BoxItemDetailComponent>;
    const route = ({ data: of({ boxItem: new BoxItem(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [BoxItemDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(BoxItemDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BoxItemDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.boxItem).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
