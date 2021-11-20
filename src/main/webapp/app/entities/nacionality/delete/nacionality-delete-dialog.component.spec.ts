jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { NacionalityService } from '../service/nacionality.service';

import { NacionalityDeleteDialogComponent } from './nacionality-delete-dialog.component';

describe('Component Tests', () => {
  describe('Nacionality Management Delete Component', () => {
    let comp: NacionalityDeleteDialogComponent;
    let fixture: ComponentFixture<NacionalityDeleteDialogComponent>;
    let service: NacionalityService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [NacionalityDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(NacionalityDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(NacionalityDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(NacionalityService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });
  });
});
