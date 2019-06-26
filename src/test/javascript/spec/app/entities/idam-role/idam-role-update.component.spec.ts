/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { UsermgmtdemoTestModule } from '../../../test.module';
import { IdamRoleUpdateComponent } from 'app/entities/idam-role/idam-role-update.component';
import { IdamRoleService } from 'app/entities/idam-role/idam-role.service';
import { IdamRole } from 'app/shared/model/idam-role.model';

describe('Component Tests', () => {
  describe('IdamRole Management Update Component', () => {
    let comp: IdamRoleUpdateComponent;
    let fixture: ComponentFixture<IdamRoleUpdateComponent>;
    let service: IdamRoleService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [UsermgmtdemoTestModule],
        declarations: [IdamRoleUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(IdamRoleUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(IdamRoleUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(IdamRoleService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new IdamRole(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new IdamRole();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
