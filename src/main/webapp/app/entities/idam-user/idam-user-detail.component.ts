import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIdamUser } from 'app/shared/model/idam-user.model';

@Component({
  selector: 'jhi-idam-user-detail',
  templateUrl: './idam-user-detail.component.html'
})
export class IdamUserDetailComponent implements OnInit {
  idamUser: IIdamUser;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ idamUser }) => {
      this.idamUser = idamUser;
    });
  }

  previousState() {
    window.history.back();
  }
}
