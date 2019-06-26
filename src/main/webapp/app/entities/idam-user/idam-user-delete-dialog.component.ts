import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IIdamUser } from 'app/shared/model/idam-user.model';
import { IdamUserService } from './idam-user.service';

@Component({
  selector: 'jhi-idam-user-delete-dialog',
  templateUrl: './idam-user-delete-dialog.component.html'
})
export class IdamUserDeleteDialogComponent {
  idamUser: IIdamUser;

  constructor(protected idamUserService: IdamUserService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.idamUserService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'idamUserListModification',
        content: 'Deleted an idamUser'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-idam-user-delete-popup',
  template: ''
})
export class IdamUserDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ idamUser }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(IdamUserDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.idamUser = idamUser;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/idam-user', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/idam-user', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
