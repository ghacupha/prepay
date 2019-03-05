import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { NgbDateAdapter } from '@ng-bootstrap/ng-bootstrap';

import { NgbDateMomentAdapter } from './util/datepicker-adapter';
import { PrepaySharedLibsModule, PrepaySharedCommonModule, HasAnyAuthorityDirective } from './';

@NgModule({
    imports: [PrepaySharedLibsModule, PrepaySharedCommonModule],
    declarations: [HasAnyAuthorityDirective],
    providers: [{ provide: NgbDateAdapter, useClass: NgbDateMomentAdapter }],
    exports: [PrepaySharedCommonModule, HasAnyAuthorityDirective],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class PrepaySharedModule {
    static forRoot() {
        return {
            ngModule: PrepaySharedModule
        };
    }
}
