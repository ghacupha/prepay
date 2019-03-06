package io.github.ghacupha.prepay.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.BigDecimalFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the Prepayment entity. This class is used in PrepaymentResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /prepayments?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PrepaymentCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter prepaymentAccount;

    private StringFilter prepaymentAccountNumber;

    private StringFilter narration;

    private StringFilter remarks;

    private StringFilter serviceOutletCode;

    private StringFilter expenseAccount;

    private StringFilter expenseAccountNumber;

    private StringFilter transactionId;

    private LocalDateFilter transactionDate;

    private BigDecimalFilter amount;

    private IntegerFilter prepaymentTerm;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getPrepaymentAccount() {
        return prepaymentAccount;
    }

    public void setPrepaymentAccount(StringFilter prepaymentAccount) {
        this.prepaymentAccount = prepaymentAccount;
    }

    public StringFilter getPrepaymentAccountNumber() {
        return prepaymentAccountNumber;
    }

    public void setPrepaymentAccountNumber(StringFilter prepaymentAccountNumber) {
        this.prepaymentAccountNumber = prepaymentAccountNumber;
    }

    public StringFilter getNarration() {
        return narration;
    }

    public void setNarration(StringFilter narration) {
        this.narration = narration;
    }

    public StringFilter getRemarks() {
        return remarks;
    }

    public void setRemarks(StringFilter remarks) {
        this.remarks = remarks;
    }

    public StringFilter getServiceOutletCode() {
        return serviceOutletCode;
    }

    public void setServiceOutletCode(StringFilter serviceOutletCode) {
        this.serviceOutletCode = serviceOutletCode;
    }

    public StringFilter getExpenseAccount() {
        return expenseAccount;
    }

    public void setExpenseAccount(StringFilter expenseAccount) {
        this.expenseAccount = expenseAccount;
    }

    public StringFilter getExpenseAccountNumber() {
        return expenseAccountNumber;
    }

    public void setExpenseAccountNumber(StringFilter expenseAccountNumber) {
        this.expenseAccountNumber = expenseAccountNumber;
    }

    public StringFilter getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(StringFilter transactionId) {
        this.transactionId = transactionId;
    }

    public LocalDateFilter getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateFilter transactionDate) {
        this.transactionDate = transactionDate;
    }

    public BigDecimalFilter getAmount() {
        return amount;
    }

    public void setAmount(BigDecimalFilter amount) {
        this.amount = amount;
    }

    public IntegerFilter getPrepaymentTerm() {
        return prepaymentTerm;
    }

    public void setPrepaymentTerm(IntegerFilter prepaymentTerm) {
        this.prepaymentTerm = prepaymentTerm;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PrepaymentCriteria that = (PrepaymentCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(prepaymentAccount, that.prepaymentAccount) &&
            Objects.equals(prepaymentAccountNumber, that.prepaymentAccountNumber) &&
            Objects.equals(narration, that.narration) &&
            Objects.equals(remarks, that.remarks) &&
            Objects.equals(serviceOutletCode, that.serviceOutletCode) &&
            Objects.equals(expenseAccount, that.expenseAccount) &&
            Objects.equals(expenseAccountNumber, that.expenseAccountNumber) &&
            Objects.equals(transactionId, that.transactionId) &&
            Objects.equals(transactionDate, that.transactionDate) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(prepaymentTerm, that.prepaymentTerm);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        prepaymentAccount,
        prepaymentAccountNumber,
        narration,
        remarks,
        serviceOutletCode,
        expenseAccount,
        expenseAccountNumber,
        transactionId,
        transactionDate,
        amount,
        prepaymentTerm
        );
    }

    @Override
    public String toString() {
        return "PrepaymentCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (prepaymentAccount != null ? "prepaymentAccount=" + prepaymentAccount + ", " : "") +
                (prepaymentAccountNumber != null ? "prepaymentAccountNumber=" + prepaymentAccountNumber + ", " : "") +
                (narration != null ? "narration=" + narration + ", " : "") +
                (remarks != null ? "remarks=" + remarks + ", " : "") +
                (serviceOutletCode != null ? "serviceOutletCode=" + serviceOutletCode + ", " : "") +
                (expenseAccount != null ? "expenseAccount=" + expenseAccount + ", " : "") +
                (expenseAccountNumber != null ? "expenseAccountNumber=" + expenseAccountNumber + ", " : "") +
                (transactionId != null ? "transactionId=" + transactionId + ", " : "") +
                (transactionDate != null ? "transactionDate=" + transactionDate + ", " : "") +
                (amount != null ? "amount=" + amount + ", " : "") +
                (prepaymentTerm != null ? "prepaymentTerm=" + prepaymentTerm + ", " : "") +
            "}";
    }

}
