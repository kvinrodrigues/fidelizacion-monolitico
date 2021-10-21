import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PointUsageConceptDetailComponent } from './point-usage-concept-detail.component';

describe('Component Tests', () => {
  describe('PointUsageConcept Management Detail Component', () => {
    let comp: PointUsageConceptDetailComponent;
    let fixture: ComponentFixture<PointUsageConceptDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PointUsageConceptDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ pointUsageConcept: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PointUsageConceptDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PointUsageConceptDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load pointUsageConcept on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.pointUsageConcept).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
