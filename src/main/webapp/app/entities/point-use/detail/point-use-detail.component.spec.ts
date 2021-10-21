import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PointUseDetailComponent } from './point-use-detail.component';

describe('Component Tests', () => {
  describe('PointUse Management Detail Component', () => {
    let comp: PointUseDetailComponent;
    let fixture: ComponentFixture<PointUseDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PointUseDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ pointUse: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PointUseDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PointUseDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load pointUse on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.pointUse).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
