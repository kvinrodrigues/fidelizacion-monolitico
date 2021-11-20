jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ClientService } from '../service/client.service';

import { ClientDeleteDialogComponent } from './client-delete-dialog.component';

describe('Component Tests', () => {
  describe('Client Management Delete Component', () => {
    let comp: ClientDeleteDialogComponent;
    let fixture: ComponentFixture<ClientDeleteDialogComponent>;
    let service: ClientService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ClientDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(ClientDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ClientDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ClientService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });
  });
});
