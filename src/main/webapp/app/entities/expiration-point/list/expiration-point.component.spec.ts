import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ExpirationPointService } from '../service/expiration-point.service';

import { ExpirationPointComponent } from './expiration-point.component';

describe('Component Tests', () => {
  describe('ExpirationPoint Management Component', () => {
    let comp: ExpirationPointComponent;
    let fixture: ComponentFixture<ExpirationPointComponent>;
    let service: ExpirationPointService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ExpirationPointComponent],
      })
        .overrideTemplate(ExpirationPointComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ExpirationPointComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ExpirationPointService);

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
      expect(comp.expirationPoints?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
