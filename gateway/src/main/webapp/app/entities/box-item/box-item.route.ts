import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { BoxItem } from 'app/shared/model/box-item.model';
import { BoxItemService } from './box-item.service';
import { BoxItemComponent } from './box-item.component';
import { BoxItemDetailComponent } from './box-item-detail.component';
import { BoxItemUpdateComponent } from './box-item-update.component';
import { BoxItemDeletePopupComponent } from './box-item-delete-dialog.component';
import { IBoxItem } from 'app/shared/model/box-item.model';

@Injectable({ providedIn: 'root' })
export class BoxItemResolve implements Resolve<IBoxItem> {
  constructor(private service: BoxItemService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IBoxItem> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<BoxItem>) => response.ok),
        map((boxItem: HttpResponse<BoxItem>) => boxItem.body)
      );
    }
    return of(new BoxItem());
  }
}

export const boxItemRoute: Routes = [
  {
    path: '',
    component: BoxItemComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gatewayApp.boxItem.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: BoxItemDetailComponent,
    resolve: {
      boxItem: BoxItemResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gatewayApp.boxItem.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: BoxItemUpdateComponent,
    resolve: {
      boxItem: BoxItemResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gatewayApp.boxItem.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: BoxItemUpdateComponent,
    resolve: {
      boxItem: BoxItemResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gatewayApp.boxItem.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const boxItemPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: BoxItemDeletePopupComponent,
    resolve: {
      boxItem: BoxItemResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gatewayApp.boxItem.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
