import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PointUseDetService } from '../service/point-use-det.service';

import { PointUseDetComponent } from './point-use-det.component';

describe('Component Tests', () => {
  describe('PointUseDet Management Component', () => {
    let comp: PointUseDetComponent;
    let fixture: ComponentFixture<PointUseDetComponent>;
    let service: PointUseDetService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PointUseDetComponent],
      })
        .overrideTemplate(PointUseDetComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PointUseDetComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(PointUseDetService);

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
      expect(comp.pointUseDets?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
