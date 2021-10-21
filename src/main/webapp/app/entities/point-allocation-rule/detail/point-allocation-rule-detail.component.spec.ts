import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PointAllocationRuleDetailComponent } from './point-allocation-rule-detail.component';

describe('Component Tests', () => {
  describe('PointAllocationRule Management Detail Component', () => {
    let comp: PointAllocationRuleDetailComponent;
    let fixture: ComponentFixture<PointAllocationRuleDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PointAllocationRuleDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ pointAllocationRule: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PointAllocationRuleDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PointAllocationRuleDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load pointAllocationRule on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.pointAllocationRule).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
