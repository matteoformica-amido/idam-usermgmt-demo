import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { UsermgmtdemoSharedModule } from 'app/shared';
import {
  IdamUserComponent,
  IdamUserDetailComponent,
  IdamUserUpdateComponent,
  IdamUserDeletePopupComponent,
  IdamUserDeleteDialogComponent,
  idamUserRoute,
  idamUserPopupRoute
} from './';

const ENTITY_STATES = [...idamUserRoute, ...idamUserPopupRoute];

@NgModule({
  imports: [UsermgmtdemoSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    IdamUserComponent,
    IdamUserDetailComponent,
    IdamUserUpdateComponent,
    IdamUserDeleteDialogComponent,
    IdamUserDeletePopupComponent
  ],
  entryComponents: [IdamUserComponent, IdamUserUpdateComponent, IdamUserDeleteDialogComponent, IdamUserDeletePopupComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class UsermgmtdemoIdamUserModule {}
