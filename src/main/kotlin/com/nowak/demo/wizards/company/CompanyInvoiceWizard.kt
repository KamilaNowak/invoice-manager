package com.nowak.demo.wizards.company

import tornadofx.*

const val SUBTITLE= "After filling all required field new invoice will be generated and email will be send to receiver"

class CompanyInvoiceWizard : Wizard("Create new company invoice", SUBTITLE ) {

    override val canGoNext = currentPageComplete
    override val canFinish = allPagesComplete

    init {
        enableStepLinks = true
        showSteps = true
        numberedSteps = true
        finishButtonTextProperty.value = "Create"

        add(CompanyDetailsWizardView::class)
        add(InvoiceDetailsWizardView::class)


    }

}
