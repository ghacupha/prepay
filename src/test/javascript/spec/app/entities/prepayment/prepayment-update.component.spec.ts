/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { PrepayTestModule } from '../../../test.module';
import { PrepaymentUpdateComponent } from 'app/entities/prepayment/prepayment-update.component';
import { PrepaymentService } from 'app/entities/prepayment/prepayment.service';
import { Prepayment } from 'app/shared/model/prepayment.model';

describe('Component Tests', () => {
    describe('Prepayment Management Update Component', () => {
        let comp: PrepaymentUpdateComponent;
        let fixture: ComponentFixture<PrepaymentUpdateComponent>;
        let service: PrepaymentService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PrepayTestModule],
                declarations: [PrepaymentUpdateComponent]
            })
                .overrideTemplate(PrepaymentUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(PrepaymentUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PrepaymentService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new Prepayment(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.prepayment = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new Prepayment();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.prepayment = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
