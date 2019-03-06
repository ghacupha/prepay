import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { IPrepayment } from 'app/shared/model/prepayment.model';
import { PrepaymentService } from './prepayment.service';

@Component({
    selector: 'jhi-prepayment-update',
    templateUrl: './prepayment-update.component.html'
})
export class PrepaymentUpdateComponent implements OnInit {
    prepayment: IPrepayment;
    isSaving: boolean;
    transactionDateDp: any;

    constructor(protected prepaymentService: PrepaymentService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ prepayment }) => {
            this.prepayment = prepayment;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.prepayment.id !== undefined) {
            this.subscribeToSaveResponse(this.prepaymentService.update(this.prepayment));
        } else {
            this.subscribeToSaveResponse(this.prepaymentService.create(this.prepayment));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IPrepayment>>) {
        result.subscribe((res: HttpResponse<IPrepayment>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
