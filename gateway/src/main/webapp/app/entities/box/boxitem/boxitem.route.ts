import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Boxitem } from 'app/shared/model/box/boxitem.model';
import { BoxitemService } from './boxitem.service';
import { BoxitemComponent } from './boxitem.component';
import { BoxitemDetailComponent } from './boxitem-detail.component';
import { BoxitemUpdateComponent } from './boxitem-update.component';
import { BoxitemDeletePopupComponent } from './boxitem-delete-dialog.component';
import { IBoxitem } from 'app/shared/model/box/boxitem.model';

@Injectable({ providedIn: 'root' })
export class BoxitemResolve implements Resolve<IBoxitem> {
  constructor(private service: BoxitemService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IBoxitem> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Boxitem>) => response.ok),
        map((boxitem: HttpResponse<Boxitem>) => boxitem.body)
      );
    }
    return of(new Boxitem());
  }
}

export const boxitemRoute: Routes = [
  {
    path: '',
    component: BoxitemComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gatewayApp.boxBoxitem.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: BoxitemDetailComponent,
    resolve: {
      boxitem: BoxitemResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gatewayApp.boxBoxitem.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: BoxitemUpdateComponent,
    resolve: {
      boxitem: BoxitemResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gatewayApp.boxBoxitem.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: BoxitemUpdateComponent,
    resolve: {
      boxitem: BoxitemResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gatewayApp.boxBoxitem.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const boxitemPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: BoxitemDeletePopupComponent,
    resolve: {
      boxitem: BoxitemResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gatewayApp.boxBoxitem.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
