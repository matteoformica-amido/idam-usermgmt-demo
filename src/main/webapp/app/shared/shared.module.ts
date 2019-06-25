import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { UsermgmtdemoSharedCommonModule, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [UsermgmtdemoSharedCommonModule],
  declarations: [HasAnyAuthorityDirective],
  exports: [UsermgmtdemoSharedCommonModule, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class UsermgmtdemoSharedModule {
  static forRoot() {
    return {
      ngModule: UsermgmtdemoSharedModule
    };
  }
}
