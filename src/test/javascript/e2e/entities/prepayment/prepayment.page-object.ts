import { element, by, ElementFinder } from 'protractor';

export class PrepaymentComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-prepayment div table .btn-danger'));
    title = element.all(by.css('jhi-prepayment div h2#page-heading span')).first();

    async clickOnCreateButton() {
        await this.createButton.click();
    }

    async clickOnLastDeleteButton() {
        await this.deleteButtons.last().click();
    }

    async countDeleteButtons() {
        return this.deleteButtons.count();
    }

    async getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class PrepaymentUpdatePage {
    pageTitle = element(by.id('jhi-prepayment-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    prepaymentAccountInput = element(by.id('field_prepaymentAccount'));
    prepaymentAccountNumberInput = element(by.id('field_prepaymentAccountNumber'));
    narrationInput = element(by.id('field_narration'));
    remarksInput = element(by.id('field_remarks'));
    serviceOutletCodeInput = element(by.id('field_serviceOutletCode'));
    expenseAccountInput = element(by.id('field_expenseAccount'));
    expenseAccountNumberInput = element(by.id('field_expenseAccountNumber'));
    transactionIdInput = element(by.id('field_transactionId'));
    transactionDateInput = element(by.id('field_transactionDate'));
    amountInput = element(by.id('field_amount'));
    prepaymentTermInput = element(by.id('field_prepaymentTerm'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setPrepaymentAccountInput(prepaymentAccount) {
        await this.prepaymentAccountInput.sendKeys(prepaymentAccount);
    }

    async getPrepaymentAccountInput() {
        return this.prepaymentAccountInput.getAttribute('value');
    }

    async setPrepaymentAccountNumberInput(prepaymentAccountNumber) {
        await this.prepaymentAccountNumberInput.sendKeys(prepaymentAccountNumber);
    }

    async getPrepaymentAccountNumberInput() {
        return this.prepaymentAccountNumberInput.getAttribute('value');
    }

    async setNarrationInput(narration) {
        await this.narrationInput.sendKeys(narration);
    }

    async getNarrationInput() {
        return this.narrationInput.getAttribute('value');
    }

    async setRemarksInput(remarks) {
        await this.remarksInput.sendKeys(remarks);
    }

    async getRemarksInput() {
        return this.remarksInput.getAttribute('value');
    }

    async setServiceOutletCodeInput(serviceOutletCode) {
        await this.serviceOutletCodeInput.sendKeys(serviceOutletCode);
    }

    async getServiceOutletCodeInput() {
        return this.serviceOutletCodeInput.getAttribute('value');
    }

    async setExpenseAccountInput(expenseAccount) {
        await this.expenseAccountInput.sendKeys(expenseAccount);
    }

    async getExpenseAccountInput() {
        return this.expenseAccountInput.getAttribute('value');
    }

    async setExpenseAccountNumberInput(expenseAccountNumber) {
        await this.expenseAccountNumberInput.sendKeys(expenseAccountNumber);
    }

    async getExpenseAccountNumberInput() {
        return this.expenseAccountNumberInput.getAttribute('value');
    }

    async setTransactionIdInput(transactionId) {
        await this.transactionIdInput.sendKeys(transactionId);
    }

    async getTransactionIdInput() {
        return this.transactionIdInput.getAttribute('value');
    }

    async setTransactionDateInput(transactionDate) {
        await this.transactionDateInput.sendKeys(transactionDate);
    }

    async getTransactionDateInput() {
        return this.transactionDateInput.getAttribute('value');
    }

    async setAmountInput(amount) {
        await this.amountInput.sendKeys(amount);
    }

    async getAmountInput() {
        return this.amountInput.getAttribute('value');
    }

    async setPrepaymentTermInput(prepaymentTerm) {
        await this.prepaymentTermInput.sendKeys(prepaymentTerm);
    }

    async getPrepaymentTermInput() {
        return this.prepaymentTermInput.getAttribute('value');
    }

    async save() {
        await this.saveButton.click();
    }

    async cancel() {
        await this.cancelButton.click();
    }

    getSaveButton(): ElementFinder {
        return this.saveButton;
    }
}

export class PrepaymentDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-prepayment-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-prepayment'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
