import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { MaterialList } from 'app/shared/model/box/material-list.model';
import { MaterialListService } from './material-list.service';
import { MaterialListComponent } from './material-list.component';
import { MaterialListDetailComponent } from './material-list-detail.component';
import { MaterialListUpdateComponent } from './material-list-update.component';
import { MaterialListDeletePopupComponent } from './material-list-delete-dialog.component';
import { IMaterialList } from 'app/shared/model/box/material-list.model';

@Injectable({ providedIn: 'root' })
export class MaterialListResolve implements Resolve<IMaterialList> {
  constructor(private service: MaterialListService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IMaterialList> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<MaterialList>) => response.ok),
        map((materialList: HttpResponse<MaterialList>) => materialList.body)
      );
    }
    return of(new MaterialList());
  }
}

export const materialListRoute: Routes = [
  {
    path: '',
    component: MaterialListComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gatewayApp.boxMaterialList.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: MaterialListDetailComponent,
    resolve: {
      materialList: MaterialListResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gatewayApp.boxMaterialList.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: MaterialListUpdateComponent,
    resolve: {
      materialList: MaterialListResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gatewayApp.boxMaterialList.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: MaterialListUpdateComponent,
    resolve: {
      materialList: MaterialListResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gatewayApp.boxMaterialList.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const materialListPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: MaterialListDeletePopupComponent,
    resolve: {
      materialList: MaterialListResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'gatewayApp.boxMaterialList.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
