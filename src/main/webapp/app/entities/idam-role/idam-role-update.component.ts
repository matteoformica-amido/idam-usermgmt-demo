import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IIdamRole, IdamRole } from 'app/shared/model/idam-role.model';
import { IdamRoleService } from './idam-role.service';
import { IIdamUser } from 'app/shared/model/idam-user.model';
import { IdamUserService } from 'app/entities/idam-user';

@Component({
  selector: 'jhi-idam-role-update',
  templateUrl: './idam-role-update.component.html'
})
export class IdamRoleUpdateComponent implements OnInit {
  isSaving: boolean;

  idamusers: IIdamUser[];

  editForm = this.fb.group({
    id: [],
    roleName: [],
    members: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected idamRoleService: IdamRoleService,
    protected idamUserService: IdamUserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ idamRole }) => {
      this.updateForm(idamRole);
    });
    this.idamUserService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IIdamUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IIdamUser[]>) => response.body)
      )
      .subscribe((res: IIdamUser[]) => (this.idamusers = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(idamRole: IIdamRole) {
    this.editForm.patchValue({
      id: idamRole.id,
      roleName: idamRole.roleName,
      members: idamRole.members
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const idamRole = this.createFromForm();
    if (idamRole.id !== undefined) {
      this.subscribeToSaveResponse(this.idamRoleService.update(idamRole));
    } else {
      this.subscribeToSaveResponse(this.idamRoleService.create(idamRole));
    }
  }

  private createFromForm(): IIdamRole {
    return {
      ...new IdamRole(),
      id: this.editForm.get(['id']).value,
      roleName: this.editForm.get(['roleName']).value,
      members: this.editForm.get(['members']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIdamRole>>) {
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

  trackIdamUserById(index: number, item: IIdamUser) {
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
