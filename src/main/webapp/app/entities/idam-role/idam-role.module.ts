import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { UsermgmtdemoSharedModule } from 'app/shared';
import {
  IdamRoleComponent,
  IdamRoleDetailComponent,
  IdamRoleUpdateComponent,
  IdamRoleDeletePopupComponent,
  IdamRoleDeleteDialogComponent,
  idamRoleRoute,
  idamRolePopupRoute
} from './';

const ENTITY_STATES = [...idamRoleRoute, ...idamRolePopupRoute];

@NgModule({
  imports: [UsermgmtdemoSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    IdamRoleComponent,
    IdamRoleDetailComponent,
    IdamRoleUpdateComponent,
    IdamRoleDeleteDialogComponent,
    IdamRoleDeletePopupComponent
  ],
  entryComponents: [IdamRoleComponent, IdamRoleUpdateComponent, IdamRoleDeleteDialogComponent, IdamRoleDeletePopupComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class UsermgmtdemoIdamRoleModule {}
