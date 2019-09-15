import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'box',
        loadChildren: './box/box/box.module#BoxBoxModule'
      },
      {
        path: 'item',
        loadChildren: './box/item/item.module#BoxItemModule'
      },
      {
        path: 'boxitem',
        loadChildren: './box/boxitem/boxitem.module#BoxBoxitemModule'
      },
      {
        path: 'material-list',
        loadChildren: () => import('./box/material-list/material-list.module').then(m => m.BoxMaterialListModule)
      },
      {
        path: 'material-list-item',
        loadChildren: () => import('./box/material-list-item/material-list-item.module').then(m => m.BoxMaterialListItemModule)
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
