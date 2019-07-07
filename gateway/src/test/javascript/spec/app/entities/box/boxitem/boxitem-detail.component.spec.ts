/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GatewayTestModule } from '../../../../test.module';
import { BoxitemDetailComponent } from 'app/entities/box/boxitem/boxitem-detail.component';
import { Boxitem } from 'app/shared/model/box/boxitem.model';

describe('Component Tests', () => {
  describe('Boxitem Management Detail Component', () => {
    let comp: BoxitemDetailComponent;
    let fixture: ComponentFixture<BoxitemDetailComponent>;
    const route = ({ data: of({ boxitem: new Boxitem(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [GatewayTestModule],
        declarations: [BoxitemDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(BoxitemDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BoxitemDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.boxitem).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
