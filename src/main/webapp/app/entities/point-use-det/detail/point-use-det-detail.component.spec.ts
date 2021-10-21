import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PointUseDetDetailComponent } from './point-use-det-detail.component';

describe('Component Tests', () => {
  describe('PointUseDet Management Detail Component', () => {
    let comp: PointUseDetDetailComponent;
    let fixture: ComponentFixture<PointUseDetDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PointUseDetDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ pointUseDet: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PointUseDetDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PointUseDetDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load pointUseDet on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.pointUseDet).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
