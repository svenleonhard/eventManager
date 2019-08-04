import { Route } from '@angular/router';
import {FinderComponent} from "app/finder/finder.component";
import {UserRouteAccessService} from "app/core";

export const finderRoute: Route = {
  path: 'finder',
  component: FinderComponent,
  data: {
    authorities: ['ROLE_USER'],
    pageTitle: 'home.title'
  },
  canActivate: [UserRouteAccessService]
};
