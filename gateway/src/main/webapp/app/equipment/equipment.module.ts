import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { GatewaySharedModule } from 'app/shared';
import { ZXingScannerModule } from '@zxing/ngx-scanner';
import { GatewayEntityModule } from 'app/entities/entity.module';
import { EquipmentComponent } from 'app/equipment/equipment.component';
import { equipmentRoute } from 'app/equipment/equipment.route';

@NgModule({
  imports: [GatewaySharedModule, RouterModule.forChild([equipmentRoute]), ZXingScannerModule, GatewayEntityModule],
  declarations: [EquipmentComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class GatewayEquipmentModule {}
