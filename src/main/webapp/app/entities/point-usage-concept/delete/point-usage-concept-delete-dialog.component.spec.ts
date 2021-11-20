jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { PointUsageConceptService } from '../service/point-usage-concept.service';

import { PointUsageConceptDeleteDialogComponent } from './point-usage-concept-delete-dialog.component';

describe('Component Tests', () => {
  describe('PointUsageConcept Management Delete Component', () => {
    let comp: PointUsageConceptDeleteDialogComponent;
    let fixture: ComponentFixture<PointUsageConceptDeleteDialogComponent>;
    let service: PointUsageConceptService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PointUsageConceptDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(PointUsageConceptDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PointUsageConceptDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(PointUsageConceptService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });
  });
});
