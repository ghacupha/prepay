/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { PrepaymentService } from 'app/entities/prepayment/prepayment.service';
import { IPrepayment, Prepayment } from 'app/shared/model/prepayment.model';

describe('Service Tests', () => {
    describe('Prepayment Service', () => {
        let injector: TestBed;
        let service: PrepaymentService;
        let httpMock: HttpTestingController;
        let elemDefault: IPrepayment;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(PrepaymentService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new Prepayment(
                0,
                'AAAAAAA',
                'AAAAAAA',
                'AAAAAAA',
                'AAAAAAA',
                'AAAAAAA',
                'AAAAAAA',
                'AAAAAAA',
                'AAAAAAA',
                currentDate,
                0,
                0
            );
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign(
                    {
                        transactionDate: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );
                service
                    .find(123)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: elemDefault }));

                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should create a Prepayment', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0,
                        transactionDate: currentDate.format(DATE_FORMAT)
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        transactionDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .create(new Prepayment(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a Prepayment', async () => {
                const returnedFromService = Object.assign(
                    {
                        prepaymentAccount: 'BBBBBB',
                        prepaymentAccountNumber: 'BBBBBB',
                        narration: 'BBBBBB',
                        remarks: 'BBBBBB',
                        serviceOutletCode: 'BBBBBB',
                        expenseAccount: 'BBBBBB',
                        expenseAccountNumber: 'BBBBBB',
                        transactionId: 'BBBBBB',
                        transactionDate: currentDate.format(DATE_FORMAT),
                        amount: 1,
                        prepaymentTerm: 1
                    },
                    elemDefault
                );

                const expected = Object.assign(
                    {
                        transactionDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .update(expected)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'PUT' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should return a list of Prepayment', async () => {
                const returnedFromService = Object.assign(
                    {
                        prepaymentAccount: 'BBBBBB',
                        prepaymentAccountNumber: 'BBBBBB',
                        narration: 'BBBBBB',
                        remarks: 'BBBBBB',
                        serviceOutletCode: 'BBBBBB',
                        expenseAccount: 'BBBBBB',
                        expenseAccountNumber: 'BBBBBB',
                        transactionId: 'BBBBBB',
                        transactionDate: currentDate.format(DATE_FORMAT),
                        amount: 1,
                        prepaymentTerm: 1
                    },
                    elemDefault
                );
                const expected = Object.assign(
                    {
                        transactionDate: currentDate
                    },
                    returnedFromService
                );
                service
                    .query(expected)
                    .pipe(
                        take(1),
                        map(resp => resp.body)
                    )
                    .subscribe(body => expect(body).toContainEqual(expected));
                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify([returnedFromService]));
                httpMock.verify();
            });

            it('should delete a Prepayment', async () => {
                const rxPromise = service.delete(123).subscribe(resp => expect(resp.ok));

                const req = httpMock.expectOne({ method: 'DELETE' });
                req.flush({ status: 200 });
            });
        });

        afterEach(() => {
            httpMock.verify();
        });
    });
});
