import { NgModule } from '@angular/core';

import { UsermgmtdemoSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
  imports: [UsermgmtdemoSharedLibsModule],
  declarations: [JhiAlertComponent, JhiAlertErrorComponent],
  exports: [UsermgmtdemoSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class UsermgmtdemoSharedCommonModule {}
