jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { PointUseService } from '../service/point-use.service';

import { PointUseDeleteDialogComponent } from './point-use-delete-dialog.component';

describe('Component Tests', () => {
  describe('PointUse Management Delete Component', () => {
    let comp: PointUseDeleteDialogComponent;
    let fixture: ComponentFixture<PointUseDeleteDialogComponent>;
    let service: PointUseService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PointUseDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(PointUseDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PointUseDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(PointUseService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });
  });
});
