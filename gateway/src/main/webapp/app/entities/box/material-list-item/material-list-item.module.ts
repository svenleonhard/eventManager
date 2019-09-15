import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { GatewaySharedModule } from 'app/shared';
import {
  MaterialListItemComponent,
  MaterialListItemDetailComponent,
  MaterialListItemUpdateComponent,
  MaterialListItemDeletePopupComponent,
  MaterialListItemDeleteDialogComponent,
  materialListItemRoute,
  materialListItemPopupRoute
} from './';

const ENTITY_STATES = [...materialListItemRoute, ...materialListItemPopupRoute];

@NgModule({
  imports: [GatewaySharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    MaterialListItemComponent,
    MaterialListItemDetailComponent,
    MaterialListItemUpdateComponent,
    MaterialListItemDeleteDialogComponent,
    MaterialListItemDeletePopupComponent
  ],
  entryComponents: [
    MaterialListItemComponent,
    MaterialListItemUpdateComponent,
    MaterialListItemDeleteDialogComponent,
    MaterialListItemDeletePopupComponent
  ],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BoxMaterialListItemModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
