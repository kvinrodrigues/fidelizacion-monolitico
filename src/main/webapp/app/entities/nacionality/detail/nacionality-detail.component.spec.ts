import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NacionalityDetailComponent } from './nacionality-detail.component';

describe('Component Tests', () => {
  describe('Nacionality Management Detail Component', () => {
    let comp: NacionalityDetailComponent;
    let fixture: ComponentFixture<NacionalityDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [NacionalityDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ nacionality: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(NacionalityDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(NacionalityDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load nacionality on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.nacionality).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
