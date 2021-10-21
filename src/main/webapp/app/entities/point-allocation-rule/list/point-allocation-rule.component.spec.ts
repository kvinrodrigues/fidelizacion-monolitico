import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PointAllocationRuleService } from '../service/point-allocation-rule.service';

import { PointAllocationRuleComponent } from './point-allocation-rule.component';

describe('Component Tests', () => {
  describe('PointAllocationRule Management Component', () => {
    let comp: PointAllocationRuleComponent;
    let fixture: ComponentFixture<PointAllocationRuleComponent>;
    let service: PointAllocationRuleService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PointAllocationRuleComponent],
      })
        .overrideTemplate(PointAllocationRuleComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PointAllocationRuleComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(PointAllocationRuleService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.pointAllocationRules?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
