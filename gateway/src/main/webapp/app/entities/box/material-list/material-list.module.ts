import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { GatewaySharedModule } from 'app/shared';
import {
  MaterialListComponent,
  MaterialListDetailComponent,
  MaterialListUpdateComponent,
  MaterialListDeletePopupComponent,
  MaterialListDeleteDialogComponent,
  materialListRoute,
  materialListPopupRoute
} from './';

const ENTITY_STATES = [...materialListRoute, ...materialListPopupRoute];

@NgModule({
  imports: [GatewaySharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    MaterialListComponent,
    MaterialListDetailComponent,
    MaterialListUpdateComponent,
    MaterialListDeleteDialogComponent,
    MaterialListDeletePopupComponent
  ],
  entryComponents: [
    MaterialListComponent,
    MaterialListUpdateComponent,
    MaterialListDeleteDialogComponent,
    MaterialListDeletePopupComponent
  ],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BoxMaterialListModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
