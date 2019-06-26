import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IIdamUser, IdamUser } from 'app/shared/model/idam-user.model';
import { IdamUserService } from './idam-user.service';

@Component({
  selector: 'jhi-idam-user-update',
  templateUrl: './idam-user-update.component.html'
})
export class IdamUserUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    uid: [],
    email: [],
    firstName: [],
    lastName: [],
    status: [],
    roles: []
  });

  constructor(protected idamUserService: IdamUserService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ idamUser }) => {
      this.updateForm(idamUser);
    });
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
}
