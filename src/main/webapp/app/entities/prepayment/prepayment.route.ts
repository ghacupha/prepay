import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Prepayment } from 'app/shared/model/prepayment.model';
import { PrepaymentService } from './prepayment.service';
import { PrepaymentComponent } from './prepayment.component';
import { PrepaymentDetailComponent } from './prepayment-detail.component';
import { PrepaymentUpdateComponent } from './prepayment-update.component';
import { PrepaymentDeletePopupComponent } from './prepayment-delete-dialog.component';
import { IPrepayment } from 'app/shared/model/prepayment.model';

@Injectable({ providedIn: 'root' })
export class PrepaymentResolve implements Resolve<IPrepayment> {
    constructor(private service: PrepaymentService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IPrepayment> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Prepayment>) => response.ok),
                map((prepayment: HttpResponse<Prepayment>) => prepayment.body)
            );
        }
        return of(new Prepayment());
    }
}

export const prepaymentRoute: Routes = [
    {
        path: '',
        component: PrepaymentComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'prepayApp.prepayment.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: PrepaymentDetailComponent,
        resolve: {
            prepayment: PrepaymentResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'prepayApp.prepayment.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: PrepaymentUpdateComponent,
        resolve: {
            prepayment: PrepaymentResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'prepayApp.prepayment.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: PrepaymentUpdateComponent,
        resolve: {
            prepayment: PrepaymentResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'prepayApp.prepayment.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const prepaymentPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: PrepaymentDeletePopupComponent,
        resolve: {
            prepayment: PrepaymentResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'prepayApp.prepayment.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
