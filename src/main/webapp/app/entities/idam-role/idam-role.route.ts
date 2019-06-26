import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { IdamRole } from 'app/shared/model/idam-role.model';
import { IdamRoleService } from './idam-role.service';
import { IdamRoleComponent } from './idam-role.component';
import { IdamRoleDetailComponent } from './idam-role-detail.component';
import { IdamRoleUpdateComponent } from './idam-role-update.component';
import { IdamRoleDeletePopupComponent } from './idam-role-delete-dialog.component';
import { IIdamRole } from 'app/shared/model/idam-role.model';

@Injectable({ providedIn: 'root' })
export class IdamRoleResolve implements Resolve<IIdamRole> {
  constructor(private service: IdamRoleService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IIdamRole> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<IdamRole>) => response.ok),
        map((idamRole: HttpResponse<IdamRole>) => idamRole.body)
      );
    }
    return of(new IdamRole());
  }
}

export const idamRoleRoute: Routes = [
  {
    path: '',
    component: IdamRoleComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'IdamRoles'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: IdamRoleDetailComponent,
    resolve: {
      idamRole: IdamRoleResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'IdamRoles'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: IdamRoleUpdateComponent,
    resolve: {
      idamRole: IdamRoleResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'IdamRoles'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: IdamRoleUpdateComponent,
    resolve: {
      idamRole: IdamRoleResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'IdamRoles'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const idamRolePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: IdamRoleDeletePopupComponent,
    resolve: {
      idamRole: IdamRoleResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'IdamRoles'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
