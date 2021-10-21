jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PointAllocationRuleService } from '../service/point-allocation-rule.service';
import { IPointAllocationRule, PointAllocationRule } from '../point-allocation-rule.model';

import { PointAllocationRuleUpdateComponent } from './point-allocation-rule-update.component';

describe('Component Tests', () => {
  describe('PointAllocationRule Management Update Component', () => {
    let comp: PointAllocationRuleUpdateComponent;
    let fixture: ComponentFixture<PointAllocationRuleUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let pointAllocationRuleService: PointAllocationRuleService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PointAllocationRuleUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PointAllocationRuleUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PointAllocationRuleUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      pointAllocationRuleService = TestBed.inject(PointAllocationRuleService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const pointAllocationRule: IPointAllocationRule = { id: 456 };

        activatedRoute.data = of({ pointAllocationRule });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(pointAllocationRule));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PointAllocationRule>>();
        const pointAllocationRule = { id: 123 };
        jest.spyOn(pointAllocationRuleService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ pointAllocationRule });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: pointAllocationRule }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(pointAllocationRuleService.update).toHaveBeenCalledWith(pointAllocationRule);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PointAllocationRule>>();
        const pointAllocationRule = new PointAllocationRule();
        jest.spyOn(pointAllocationRuleService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ pointAllocationRule });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: pointAllocationRule }));
        saveSubject.complete();

        // THEN
        expect(pointAllocationRuleService.create).toHaveBeenCalledWith(pointAllocationRule);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<PointAllocationRule>>();
        const pointAllocationRule = { id: 123 };
        jest.spyOn(pointAllocationRuleService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ pointAllocationRule });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(pointAllocationRuleService.update).toHaveBeenCalledWith(pointAllocationRule);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
