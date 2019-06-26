import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIdamRole } from 'app/shared/model/idam-role.model';

@Component({
  selector: 'jhi-idam-role-detail',
  templateUrl: './idam-role-detail.component.html'
})
export class IdamRoleDetailComponent implements OnInit {
  idamRole: IIdamRole;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ idamRole }) => {
      this.idamRole = idamRole;
    });
  }

  previousState() {
    window.history.back();
  }
}
