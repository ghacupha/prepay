import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { PrepaySharedModule } from 'app/shared';
import {
    PrepaymentComponent,
    PrepaymentDetailComponent,
    PrepaymentUpdateComponent,
    PrepaymentDeletePopupComponent,
    PrepaymentDeleteDialogComponent,
    prepaymentRoute,
    prepaymentPopupRoute
} from './';

const ENTITY_STATES = [...prepaymentRoute, ...prepaymentPopupRoute];

@NgModule({
    imports: [PrepaySharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        PrepaymentComponent,
        PrepaymentDetailComponent,
        PrepaymentUpdateComponent,
        PrepaymentDeleteDialogComponent,
        PrepaymentDeletePopupComponent
    ],
    entryComponents: [PrepaymentComponent, PrepaymentUpdateComponent, PrepaymentDeleteDialogComponent, PrepaymentDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PrepayPrepaymentModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
