import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { BagOfPointService } from '../service/bag-of-point.service';

import { BagOfPointComponent } from './bag-of-point.component';

describe('Component Tests', () => {
  describe('BagOfPoint Management Component', () => {
    let comp: BagOfPointComponent;
    let fixture: ComponentFixture<BagOfPointComponent>;
    let service: BagOfPointService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [BagOfPointComponent],
      })
        .overrideTemplate(BagOfPointComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BagOfPointComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(BagOfPointService);

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
      expect(comp.bagOfPoints?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
