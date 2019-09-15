import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { MaterialListItem } from 'app/shared/model/box/material-list-item.model';
import { MaterialListItemService } from './material-list-item.service';
import { MaterialListItemComponent } from './material-list-item.component';
import { MaterialListItemDetailComponent } from './material-list-item-detail.component';
import { MaterialListItemUpdateComponent } from './material-list-item-update.component';
import { MaterialListItemDeletePopupComponent } from './material-list-item-delete-dialog.component';
import { IMaterialListItem } from 'app/shared/model/box/material-list-item.model';

@Injectable({ providedIn: 'root' })
export class MaterialListItemResolve implements Resolve<IMaterialListItem> {
  constructor(private service: MaterialListItemService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IMaterialListItem> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<MaterialListItem>) => response.ok),
        map((materialListItem: HttpResponse<MaterialListItem>) => materialListItem.body)
      );
    }
    return of(new MaterialListItem());
  }
}

export const materialListItemRoute: Routes = [
  {
    path: '',
    component: MaterialListItemComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gatewayApp.boxMaterialListItem.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: MaterialListItemDetailComponent,
    resolve: {
      materialListItem: MaterialListItemResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gatewayApp.boxMaterialListItem.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: MaterialListItemUpdateComponent,
    resolve: {
      materialListItem: MaterialListItemResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gatewayApp.boxMaterialListItem.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: MaterialListItemUpdateComponent,
    resolve: {
      materialListItem: MaterialListItemResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gatewayApp.boxMaterialListItem.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const materialListItemPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: MaterialListItemDeletePopupComponent,
    resolve: {
      materialListItem: MaterialListItemResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gatewayApp.boxMaterialListItem.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
