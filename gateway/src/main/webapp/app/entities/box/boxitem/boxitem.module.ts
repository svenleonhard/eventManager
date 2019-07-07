import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { GatewaySharedModule } from 'app/shared';
import {
  BoxitemComponent,
  BoxitemDetailComponent,
  BoxitemUpdateComponent,
  BoxitemDeletePopupComponent,
  BoxitemDeleteDialogComponent,
  boxitemRoute,
  boxitemPopupRoute
} from './';

const ENTITY_STATES = [...boxitemRoute, ...boxitemPopupRoute];

@NgModule({
  imports: [GatewaySharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    BoxitemComponent,
    BoxitemDetailComponent,
    BoxitemUpdateComponent,
    BoxitemDeleteDialogComponent,
    BoxitemDeletePopupComponent
  ],
  entryComponents: [BoxitemComponent, BoxitemUpdateComponent, BoxitemDeleteDialogComponent, BoxitemDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BoxBoxitemModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey !== undefined) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
