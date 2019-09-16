import { Route } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { EquipmentComponent } from 'app/equipment/equipment.component';

export const equipmentRoute: Route = {
  path: 'equipment',
  component: EquipmentComponent,
  data: {
    authorities: ['ROLE_USER'],
    pageTitle: 'home.title'
  },
  canActivate: [UserRouteAccessService]
};
