import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GatewaySharedModule } from 'app/shared';
import {FinderComponent} from "app/finder/finder.component";
import {finderRoute} from "app/finder/finder.route";

@NgModule({
  imports: [GatewaySharedModule, RouterModule.forChild([finderRoute])],
  declarations: [FinderComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GatewayFinderModule {}
