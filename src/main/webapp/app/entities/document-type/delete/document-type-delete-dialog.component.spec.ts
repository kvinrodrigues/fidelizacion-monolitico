jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { DocumentTypeService } from '../service/document-type.service';

import { DocumentTypeDeleteDialogComponent } from './document-type-delete-dialog.component';

describe('Component Tests', () => {
  describe('DocumentType Management Delete Component', () => {
    let comp: DocumentTypeDeleteDialogComponent;
    let fixture: ComponentFixture<DocumentTypeDeleteDialogComponent>;
    let service: DocumentTypeService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DocumentTypeDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(DocumentTypeDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DocumentTypeDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(DocumentTypeService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });
  });
});
