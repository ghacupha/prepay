import { Moment } from 'moment';

export interface IPrepayment {
    id?: number;
    prepaymentAccount?: string;
    prepaymentAccountNumber?: string;
    narration?: string;
    remarks?: string;
    serviceOutletCode?: string;
    expenseAccount?: string;
    expenseAccountNumber?: string;
    transactionId?: string;
    transactionDate?: Moment;
    amount?: number;
    prepaymentTerm?: number;
}

export class Prepayment implements IPrepayment {
    constructor(
        public id?: number,
        public prepaymentAccount?: string,
        public prepaymentAccountNumber?: string,
        public narration?: string,
        public remarks?: string,
        public serviceOutletCode?: string,
        public expenseAccount?: string,
        public expenseAccountNumber?: string,
        public transactionId?: string,
        public transactionDate?: Moment,
        public amount?: number,
        public prepaymentTerm?: number
    ) {}
}
