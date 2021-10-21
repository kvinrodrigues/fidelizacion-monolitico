import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ExpirationPointDetailComponent } from './expiration-point-detail.component';

describe('Component Tests', () => {
  describe('ExpirationPoint Management Detail Component', () => {
    let comp: ExpirationPointDetailComponent;
    let fixture: ComponentFixture<ExpirationPointDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ExpirationPointDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ expirationPoint: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ExpirationPointDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ExpirationPointDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load expirationPoint on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.expirationPoint).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
