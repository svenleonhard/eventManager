import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { GatewaySharedModule } from 'app/shared';
import {
  BoxComponent,
  BoxDetailComponent,
  BoxUpdateComponent,
  BoxDeletePopupComponent,
  BoxDeleteDialogComponent,
  boxRoute,
  boxPopupRoute
} from './';

const ENTITY_STATES = [...boxRoute, ...boxPopupRoute];

@NgModule({
  imports: [GatewaySharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [BoxComponent, BoxDetailComponent, BoxUpdateComponent, BoxDeleteDialogComponent, BoxDeletePopupComponent],
  entryComponents: [BoxComponent, BoxUpdateComponent, BoxDeleteDialogComponent, BoxDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GatewayBoxModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
