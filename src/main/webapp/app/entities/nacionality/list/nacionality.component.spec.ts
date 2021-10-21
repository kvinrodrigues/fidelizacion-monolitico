import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { NacionalityService } from '../service/nacionality.service';

import { NacionalityComponent } from './nacionality.component';

describe('Component Tests', () => {
  describe('Nacionality Management Component', () => {
    let comp: NacionalityComponent;
    let fixture: ComponentFixture<NacionalityComponent>;
    let service: NacionalityService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [NacionalityComponent],
      })
        .overrideTemplate(NacionalityComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(NacionalityComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(NacionalityService);

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
      expect(comp.nacionalities?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
