import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DocumentTypeService } from '../service/document-type.service';

import { DocumentTypeComponent } from './document-type.component';

describe('Component Tests', () => {
  describe('DocumentType Management Component', () => {
    let comp: DocumentTypeComponent;
    let fixture: ComponentFixture<DocumentTypeComponent>;
    let service: DocumentTypeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DocumentTypeComponent],
      })
        .overrideTemplate(DocumentTypeComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DocumentTypeComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(DocumentTypeService);

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
      expect(comp.documentTypes?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
