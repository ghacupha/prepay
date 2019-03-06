import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPrepayment } from 'app/shared/model/prepayment.model';

type EntityResponseType = HttpResponse<IPrepayment>;
type EntityArrayResponseType = HttpResponse<IPrepayment[]>;

@Injectable({ providedIn: 'root' })
export class PrepaymentService {
    public resourceUrl = SERVER_API_URL + 'api/prepayments';
    public resourceSearchUrl = SERVER_API_URL + 'api/_search/prepayments';

    constructor(protected http: HttpClient) {}

    create(prepayment: IPrepayment): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(prepayment);
        return this.http
            .post<IPrepayment>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(prepayment: IPrepayment): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(prepayment);
        return this.http
            .put<IPrepayment>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IPrepayment>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IPrepayment[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IPrepayment[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(prepayment: IPrepayment): IPrepayment {
        const copy: IPrepayment = Object.assign({}, prepayment, {
            transactionDate:
                prepayment.transactionDate != null && prepayment.transactionDate.isValid()
                    ? prepayment.transactionDate.format(DATE_FORMAT)
                    : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.transactionDate = res.body.transactionDate != null ? moment(res.body.transactionDate) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((prepayment: IPrepayment) => {
                prepayment.transactionDate = prepayment.transactionDate != null ? moment(prepayment.transactionDate) : null;
            });
        }
        return res;
    }
}
