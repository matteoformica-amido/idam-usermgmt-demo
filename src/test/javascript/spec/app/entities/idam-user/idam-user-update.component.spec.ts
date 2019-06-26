/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { UsermgmtdemoTestModule } from '../../../test.module';
import { IdamUserUpdateComponent } from 'app/entities/idam-user/idam-user-update.component';
import { IdamUserService } from 'app/entities/idam-user/idam-user.service';
import { IdamUser } from 'app/shared/model/idam-user.model';

describe('Component Tests', () => {
  describe('IdamUser Management Update Component', () => {
    let comp: IdamUserUpdateComponent;
    let fixture: ComponentFixture<IdamUserUpdateComponent>;
    let service: IdamUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [UsermgmtdemoTestModule],
        declarations: [IdamUserUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(IdamUserUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(IdamUserUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(IdamUserService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new IdamUser(123);
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
        const entity = new IdamUser();
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
