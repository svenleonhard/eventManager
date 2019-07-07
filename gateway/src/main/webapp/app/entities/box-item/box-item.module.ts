import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { GatewaySharedModule } from 'app/shared';
import {
  BoxItemComponent,
  BoxItemDetailComponent,
  BoxItemUpdateComponent,
  BoxItemDeletePopupComponent,
  BoxItemDeleteDialogComponent,
  boxItemRoute,
  boxItemPopupRoute
} from './';

const ENTITY_STATES = [...boxItemRoute, ...boxItemPopupRoute];

@NgModule({
  imports: [GatewaySharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    BoxItemComponent,
    BoxItemDetailComponent,
    BoxItemUpdateComponent,
    BoxItemDeleteDialogComponent,
    BoxItemDeletePopupComponent
  ],
  entryComponents: [BoxItemComponent, BoxItemUpdateComponent, BoxItemDeleteDialogComponent, BoxItemDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GatewayBoxItemModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
