/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { UsermgmtdemoTestModule } from '../../../test.module';
import { IdamUserDetailComponent } from 'app/entities/idam-user/idam-user-detail.component';
import { IdamUser } from 'app/shared/model/idam-user.model';

describe('Component Tests', () => {
  describe('IdamUser Management Detail Component', () => {
    let comp: IdamUserDetailComponent;
    let fixture: ComponentFixture<IdamUserDetailComponent>;
    const route = ({ data: of({ idamUser: new IdamUser(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [UsermgmtdemoTestModule],
        declarations: [IdamUserDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(IdamUserDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(IdamUserDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.idamUser).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
