import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PointUsageConceptService } from '../service/point-usage-concept.service';

import { PointUsageConceptComponent } from './point-usage-concept.component';

describe('Component Tests', () => {
  describe('PointUsageConcept Management Component', () => {
    let comp: PointUsageConceptComponent;
    let fixture: ComponentFixture<PointUsageConceptComponent>;
    let service: PointUsageConceptService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PointUsageConceptComponent],
      })
        .overrideTemplate(PointUsageConceptComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PointUsageConceptComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(PointUsageConceptService);

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
      expect(comp.pointUsageConcepts?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
