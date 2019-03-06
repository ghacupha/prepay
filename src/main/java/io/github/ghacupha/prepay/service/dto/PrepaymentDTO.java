package io.github.ghacupha.prepay.service.dto;
import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the Prepayment entity.
 */
public class PrepaymentDTO implements Serializable {

    private Long id;

    @NotNull
    private String prepaymentAccount;

    @NotNull
    @Size(min = 13)
    private String prepaymentAccountNumber;

    @NotNull
    private String narration;

    private String remarks;

    @NotNull
    @Size(min = 3, max = 3)
    private String serviceOutletCode;

    @NotNull
    private String expenseAccount;

    @NotNull
    @Size(min = 13, max = 13)
    private String expenseAccountNumber;

    @NotNull
    private String transactionId;

    @NotNull
    private LocalDate transactionDate;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private Integer prepaymentTerm;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrepaymentAccount() {
        return prepaymentAccount;
    }

    public void setPrepaymentAccount(String prepaymentAccount) {
        this.prepaymentAccount = prepaymentAccount;
    }

    public String getPrepaymentAccountNumber() {
        return prepaymentAccountNumber;
    }

    public void setPrepaymentAccountNumber(String prepaymentAccountNumber) {
        this.prepaymentAccountNumber = prepaymentAccountNumber;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getServiceOutletCode() {
        return serviceOutletCode;
    }

    public void setServiceOutletCode(String serviceOutletCode) {
        this.serviceOutletCode = serviceOutletCode;
    }

    public String getExpenseAccount() {
        return expenseAccount;
    }

    public void setExpenseAccount(String expenseAccount) {
        this.expenseAccount = expenseAccount;
    }

    public String getExpenseAccountNumber() {
        return expenseAccountNumber;
    }

    public void setExpenseAccountNumber(String expenseAccountNumber) {
        this.expenseAccountNumber = expenseAccountNumber;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getPrepaymentTerm() {
        return prepaymentTerm;
    }

    public void setPrepaymentTerm(Integer prepaymentTerm) {
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

        PrepaymentDTO prepaymentDTO = (PrepaymentDTO) o;
        if (prepaymentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), prepaymentDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PrepaymentDTO{" +
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
