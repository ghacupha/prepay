import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPrepayment } from 'app/shared/model/prepayment.model';
import { PrepaymentService } from './prepayment.service';

@Component({
    selector: 'jhi-prepayment-delete-dialog',
    templateUrl: './prepayment-delete-dialog.component.html'
})
export class PrepaymentDeleteDialogComponent {
    prepayment: IPrepayment;

    constructor(
        protected prepaymentService: PrepaymentService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.prepaymentService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'prepaymentListModification',
                content: 'Deleted an prepayment'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-prepayment-delete-popup',
    template: ''
})
export class PrepaymentDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ prepayment }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(PrepaymentDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.prepayment = prepayment;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/prepayment', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/prepayment', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
