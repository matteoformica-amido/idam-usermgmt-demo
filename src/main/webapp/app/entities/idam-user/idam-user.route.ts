import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { IdamUser } from 'app/shared/model/idam-user.model';
import { IdamUserService } from './idam-user.service';
import { IdamUserComponent } from './idam-user.component';
import { IdamUserDetailComponent } from './idam-user-detail.component';
import { IdamUserUpdateComponent } from './idam-user-update.component';
import { IdamUserDeletePopupComponent } from './idam-user-delete-dialog.component';
import { IIdamUser } from 'app/shared/model/idam-user.model';

@Injectable({ providedIn: 'root' })
export class IdamUserResolve implements Resolve<IIdamUser> {
  constructor(private service: IdamUserService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IIdamUser> {
    const id = route.params['id'] ? route.params['id'] : null;
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<IdamUser>) => response.ok),
        map((idamUser: HttpResponse<IdamUser>) => idamUser.body)
      );
    }
    return of(new IdamUser());
  }
}

export const idamUserRoute: Routes = [
  {
    path: '',
    component: IdamUserComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'IdamUsers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: IdamUserDetailComponent,
    resolve: {
      idamUser: IdamUserResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'IdamUsers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: IdamUserUpdateComponent,
    resolve: {
      idamUser: IdamUserResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'IdamUsers'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: IdamUserUpdateComponent,
    resolve: {
      idamUser: IdamUserResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'IdamUsers'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const idamUserPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: IdamUserDeletePopupComponent,
    resolve: {
      idamUser: IdamUserResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'IdamUsers'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
