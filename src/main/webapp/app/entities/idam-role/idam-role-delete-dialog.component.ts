import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IIdamRole } from 'app/shared/model/idam-role.model';
import { IdamRoleService } from './idam-role.service';

@Component({
  selector: 'jhi-idam-role-delete-dialog',
  templateUrl: './idam-role-delete-dialog.component.html'
})
export class IdamRoleDeleteDialogComponent {
  idamRole: IIdamRole;

  constructor(protected idamRoleService: IdamRoleService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.idamRoleService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'idamRoleListModification',
        content: 'Deleted an idamRole'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-idam-role-delete-popup',
  template: ''
})
export class IdamRoleDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ idamRole }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(IdamRoleDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.idamRole = idamRole;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/idam-role', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/idam-role', { outlets: { popup: null } }]);
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
