package io.github.ghacupha.prepay.domain;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Prepayment.
 */
@Entity
@Table(name = "prepayment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "prepayment")
public class Prepayment implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 13)
    @Column(name = "prepayment_account", nullable = false)
    private String prepaymentAccount;

    @NotNull
    @Size(min = 13)
    @Column(name = "prepayment_account_number", nullable = false)
    private String prepaymentAccountNumber;

    @NotNull
    @Column(name = "narration", nullable = false)
    private String narration;

    @Column(name = "remarks")
    private String remarks;

    @NotNull
    @Size(min = 3, max = 3)
    @Column(name = "service_outlet_code", length = 3, nullable = false)
    private String serviceOutletCode;

    @NotNull
    @Column(name = "expense_account", nullable = false)
    private String expenseAccount;

    @NotNull
    @Size(min = 13, max = 13)
    @Column(name = "expense_account_number", length = 13, nullable = false)
    private String expenseAccountNumber;

    @NotNull
    @Column(name = "transaction_id", nullable = false)
    private String transactionId;

    @NotNull
    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @NotNull
    @Column(name = "amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @NotNull
    @Column(name = "prepayment_term", nullable = false)
    private Integer prepaymentTerm;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrepaymentAccount() {
        return prepaymentAccount;
    }

    public Prepayment prepaymentAccount(String prepaymentAccount) {
        this.prepaymentAccount = prepaymentAccount;
        return this;
    }

    public void setPrepaymentAccount(String prepaymentAccount) {
        this.prepaymentAccount = prepaymentAccount;
    }

    public String getPrepaymentAccountNumber() {
        return prepaymentAccountNumber;
    }

    public Prepayment prepaymentAccountNumber(String prepaymentAccountNumber) {
        this.prepaymentAccountNumber = prepaymentAccountNumber;
        return this;
    }

    public void setPrepaymentAccountNumber(String prepaymentAccountNumber) {
        this.prepaymentAccountNumber = prepaymentAccountNumber;
    }

    public String getNarration() {
        return narration;
    }

    public Prepayment narration(String narration) {
        this.narration = narration;
        return this;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getRemarks() {
        return remarks;
    }

    public Prepayment remarks(String remarks) {
        this.remarks = remarks;
        return this;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getServiceOutletCode() {
        return serviceOutletCode;
    }

    public Prepayment serviceOutletCode(String serviceOutletCode) {
        this.serviceOutletCode = serviceOutletCode;
        return this;
    }

    public void setServiceOutletCode(String serviceOutletCode) {
        this.serviceOutletCode = serviceOutletCode;
    }

    public String getExpenseAccount() {
        return expenseAccount;
    }

    public Prepayment expenseAccount(String expenseAccount) {
        this.expenseAccount = expenseAccount;
        return this;
    }

    public void setExpenseAccount(String expenseAccount) {
        this.expenseAccount = expenseAccount;
    }

    public String getExpenseAccountNumber() {
        return expenseAccountNumber;
    }

    public Prepayment expenseAccountNumber(String expenseAccountNumber) {
        this.expenseAccountNumber = expenseAccountNumber;
        return this;
    }

    public void setExpenseAccountNumber(String expenseAccountNumber) {
        this.expenseAccountNumber = expenseAccountNumber;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public Prepayment transactionId(String transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public Prepayment transactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
        return this;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Prepayment amount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getPrepaymentTerm() {
        return prepaymentTerm;
    }

    public Prepayment prepaymentTerm(Integer prepaymentTerm) {
        this.prepaymentTerm = prepaymentTerm;
        return this;
    }

    public void setPrepaymentTerm(Integer prepaymentTerm) {
        this.prepaymentTerm = prepaymentTerm;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Prepayment prepayment = (Prepayment) o;
        if (prepayment.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), prepayment.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Prepayment{" +
            "id=" + getId() +
            ", prepaymentAccount='" + getPrepaymentAccount() + "'" +
            ", prepaymentAccountNumber='" + getPrepaymentAccountNumber() + "'" +
            ", narration='" + getNarration() + "'" +
            ", remarks='" + getRemarks() + "'" +
            ", serviceOutletCode='" + getServiceOutletCode() + "'" +
            ", expenseAccount='" + getExpenseAccount() + "'" +
            ", expenseAccountNumber='" + getExpenseAccountNumber() + "'" +
            ", transactionId='" + getTransactionId() + "'" +
            ", transactionDate='" + getTransactionDate() + "'" +
            ", amount=" + getAmount() +
            ", prepaymentTerm=" + getPrepaymentTerm() +
            "}";
    }
}
