import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IIdamUser, IdamUser } from 'app/shared/model/idam-user.model';
import { IdamUserService } from './idam-user.service';
import { IIdamRole } from 'app/shared/model/idam-role.model';
import { IdamRoleService } from 'app/entities/idam-role';

@Component({
  selector: 'jhi-idam-user-update',
  templateUrl: './idam-user-update.component.html'
})
export class IdamUserUpdateComponent implements OnInit {
  isSaving: boolean;

  idamroles: IIdamRole[];

  editForm = this.fb.group({
    id: [],
    uid: [],
    email: [],
    firstName: [],
    lastName: [],
    status: [],
    roles: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected idamUserService: IdamUserService,
    protected idamRoleService: IdamRoleService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ idamUser }) => {
      this.updateForm(idamUser);
    });
    this.idamRoleService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IIdamRole[]>) => mayBeOk.ok),
        map((response: HttpResponse<IIdamRole[]>) => response.body)
      )
      .subscribe((res: IIdamRole[]) => (this.idamroles = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(idamUser: IIdamUser) {
    this.editForm.patchValue({
      id: idamUser.id,
      uid: idamUser.uid,
      email: idamUser.email,
      firstName: idamUser.firstName,
      lastName: idamUser.lastName,
      status: idamUser.status,
      roles: idamUser.roles
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const idamUser = this.createFromForm();
    if (idamUser.id !== undefined) {
      this.subscribeToSaveResponse(this.idamUserService.update(idamUser));
    } else {
      this.subscribeToSaveResponse(this.idamUserService.create(idamUser));
    }
  }

  private createFromForm(): IIdamUser {
    return {
      ...new IdamUser(),
      id: this.editForm.get(['id']).value,
      uid: this.editForm.get(['uid']).value,
      email: this.editForm.get(['email']).value,
      firstName: this.editForm.get(['firstName']).value,
      lastName: this.editForm.get(['lastName']).value,
      status: this.editForm.get(['status']).value,
      roles: this.editForm.get(['roles']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIdamUser>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackIdamRoleById(index: number, item: IIdamRole) {
    return item.id;
  }

  getSelected(selectedVals: Array<any>, option: any) {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
