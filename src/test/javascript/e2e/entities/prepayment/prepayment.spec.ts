/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { PrepaymentComponentsPage, PrepaymentDeleteDialog, PrepaymentUpdatePage } from './prepayment.page-object';

const expect = chai.expect;

describe('Prepayment e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let prepaymentUpdatePage: PrepaymentUpdatePage;
    let prepaymentComponentsPage: PrepaymentComponentsPage;
    let prepaymentDeleteDialog: PrepaymentDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.loginWithOAuth('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Prepayments', async () => {
        await navBarPage.goToEntity('prepayment');
        prepaymentComponentsPage = new PrepaymentComponentsPage();
        await browser.wait(ec.visibilityOf(prepaymentComponentsPage.title), 5000);
        expect(await prepaymentComponentsPage.getTitle()).to.eq('prepayApp.prepayment.home.title');
    });

    it('should load create Prepayment page', async () => {
        await prepaymentComponentsPage.clickOnCreateButton();
        prepaymentUpdatePage = new PrepaymentUpdatePage();
        expect(await prepaymentUpdatePage.getPageTitle()).to.eq('prepayApp.prepayment.home.createOrEditLabel');
        await prepaymentUpdatePage.cancel();
    });

    it('should create and save Prepayments', async () => {
        const nbButtonsBeforeCreate = await prepaymentComponentsPage.countDeleteButtons();

        await prepaymentComponentsPage.clickOnCreateButton();
        await promise.all([
            prepaymentUpdatePage.setPrepaymentAccountInput('prepaymentAccount'),
            prepaymentUpdatePage.setPrepaymentAccountNumberInput('prepaymentAccountNumber'),
            prepaymentUpdatePage.setNarrationInput('narration'),
            prepaymentUpdatePage.setRemarksInput('remarks'),
            prepaymentUpdatePage.setServiceOutletCodeInput('serviceOutletCode'),
            prepaymentUpdatePage.setExpenseAccountInput('expenseAccount'),
            prepaymentUpdatePage.setExpenseAccountNumberInput('expenseAccountNumber'),
            prepaymentUpdatePage.setTransactionIdInput('transactionId'),
            prepaymentUpdatePage.setTransactionDateInput('2000-12-31'),
            prepaymentUpdatePage.setAmountInput('5'),
            prepaymentUpdatePage.setPrepaymentTermInput('5')
        ]);
        expect(await prepaymentUpdatePage.getPrepaymentAccountInput()).to.eq('prepaymentAccount');
        expect(await prepaymentUpdatePage.getPrepaymentAccountNumberInput()).to.eq('prepaymentAccountNumber');
        expect(await prepaymentUpdatePage.getNarrationInput()).to.eq('narration');
        expect(await prepaymentUpdatePage.getRemarksInput()).to.eq('remarks');
        expect(await prepaymentUpdatePage.getServiceOutletCodeInput()).to.eq('serviceOutletCode');
        expect(await prepaymentUpdatePage.getExpenseAccountInput()).to.eq('expenseAccount');
        expect(await prepaymentUpdatePage.getExpenseAccountNumberInput()).to.eq('expenseAccountNumber');
        expect(await prepaymentUpdatePage.getTransactionIdInput()).to.eq('transactionId');
        expect(await prepaymentUpdatePage.getTransactionDateInput()).to.eq('2000-12-31');
        expect(await prepaymentUpdatePage.getAmountInput()).to.eq('5');
        expect(await prepaymentUpdatePage.getPrepaymentTermInput()).to.eq('5');
        await prepaymentUpdatePage.save();
        expect(await prepaymentUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await prepaymentComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last Prepayment', async () => {
        const nbButtonsBeforeDelete = await prepaymentComponentsPage.countDeleteButtons();
        await prepaymentComponentsPage.clickOnLastDeleteButton();

        prepaymentDeleteDialog = new PrepaymentDeleteDialog();
        expect(await prepaymentDeleteDialog.getDialogTitle()).to.eq('prepayApp.prepayment.delete.question');
        await prepaymentDeleteDialog.clickOnConfirmButton();

        expect(await prepaymentComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
