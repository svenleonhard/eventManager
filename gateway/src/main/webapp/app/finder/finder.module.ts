import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GatewaySharedModule } from 'app/shared';
import { FinderComponent } from 'app/finder/finder.component';
import { ZXingScannerModule } from '@zxing/ngx-scanner';
import { GatewayEntityModule } from 'app/entities/entity.module';
import { finderRoute } from 'app/finder/finder.route';

@NgModule({
  imports: [GatewaySharedModule, RouterModule.forChild([finderRoute]), ZXingScannerModule, GatewayEntityModule],
  declarations: [FinderComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GatewayFinderModule {}
