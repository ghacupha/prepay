import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPrepayment } from 'app/shared/model/prepayment.model';

@Component({
    selector: 'jhi-prepayment-detail',
    templateUrl: './prepayment-detail.component.html'
})
export class PrepaymentDetailComponent implements OnInit {
    prepayment: IPrepayment;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ prepayment }) => {
            this.prepayment = prepayment;
        });
    }

    previousState() {
        window.history.back();
    }
}
