jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ExpirationPointService } from '../service/expiration-point.service';

import { ExpirationPointDeleteDialogComponent } from './expiration-point-delete-dialog.component';

describe('Component Tests', () => {
  describe('ExpirationPoint Management Delete Component', () => {
    let comp: ExpirationPointDeleteDialogComponent;
    let fixture: ComponentFixture<ExpirationPointDeleteDialogComponent>;
    let service: ExpirationPointService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ExpirationPointDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(ExpirationPointDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ExpirationPointDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ExpirationPointService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });
  });
});
