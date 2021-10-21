import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BagOfPointDetailComponent } from './bag-of-point-detail.component';

describe('Component Tests', () => {
  describe('BagOfPoint Management Detail Component', () => {
    let comp: BagOfPointDetailComponent;
    let fixture: ComponentFixture<BagOfPointDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [BagOfPointDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ bagOfPoint: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(BagOfPointDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BagOfPointDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load bagOfPoint on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.bagOfPoint).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
