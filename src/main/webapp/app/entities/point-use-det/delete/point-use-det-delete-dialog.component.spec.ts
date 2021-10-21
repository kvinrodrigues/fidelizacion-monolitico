jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { PointUseDetService } from '../service/point-use-det.service';

import { PointUseDetDeleteDialogComponent } from './point-use-det-delete-dialog.component';

describe('Component Tests', () => {
  describe('PointUseDet Management Delete Component', () => {
    let comp: PointUseDetDeleteDialogComponent;
    let fixture: ComponentFixture<PointUseDetDeleteDialogComponent>;
    let service: PointUseDetService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PointUseDetDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(PointUseDetDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PointUseDetDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(PointUseDetService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({})));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        jest.spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
