/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PrepayTestModule } from '../../../test.module';
import { PrepaymentDetailComponent } from 'app/entities/prepayment/prepayment-detail.component';
import { Prepayment } from 'app/shared/model/prepayment.model';

describe('Component Tests', () => {
    describe('Prepayment Management Detail Component', () => {
        let comp: PrepaymentDetailComponent;
        let fixture: ComponentFixture<PrepaymentDetailComponent>;
        const route = ({ data: of({ prepayment: new Prepayment(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [PrepayTestModule],
                declarations: [PrepaymentDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(PrepaymentDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(PrepaymentDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.prepayment).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
