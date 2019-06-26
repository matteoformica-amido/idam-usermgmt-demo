/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UsermgmtdemoTestModule } from '../../../test.module';
import { IdamRoleDetailComponent } from 'app/entities/idam-role/idam-role-detail.component';
import { IdamRole } from 'app/shared/model/idam-role.model';

describe('Component Tests', () => {
  describe('IdamRole Management Detail Component', () => {
    let comp: IdamRoleDetailComponent;
    let fixture: ComponentFixture<IdamRoleDetailComponent>;
    const route = ({ data: of({ idamRole: new IdamRole(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [UsermgmtdemoTestModule],
        declarations: [IdamRoleDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(IdamRoleDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(IdamRoleDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.idamRole).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
