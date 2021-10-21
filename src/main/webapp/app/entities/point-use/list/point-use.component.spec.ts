import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PointUseService } from '../service/point-use.service';

import { PointUseComponent } from './point-use.component';

describe('Component Tests', () => {
  describe('PointUse Management Component', () => {
    let comp: PointUseComponent;
    let fixture: ComponentFixture<PointUseComponent>;
    let service: PointUseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PointUseComponent],
      })
        .overrideTemplate(PointUseComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PointUseComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(PointUseService);

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
      expect(comp.pointUses?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
