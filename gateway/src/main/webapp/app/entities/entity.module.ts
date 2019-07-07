import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'box',
        loadChildren: './box/box.module#GatewayBoxModule'
      },
      {
        path: 'item',
        loadChildren: './item/item.module#GatewayItemModule'
      },
      {
        path: 'box-item',
        loadChildren: './box-item/box-item.module#GatewayBoxItemModule'
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ],
  declarations: [],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GatewayEntityModule {}
