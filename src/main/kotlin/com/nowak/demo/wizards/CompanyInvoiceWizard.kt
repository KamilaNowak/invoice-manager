package com.nowak.demo.wizards

import javafx.beans.binding.BooleanExpression
import tornadofx.*

class CompanyInvoiceWizard : Wizard("Create new company invoice", "After filling all required field new invoice will be generated and email will be send to receiver") {
    override val canGoNext = currentPageComplete
    override val canFinish = allPagesComplete

    init {
        enableStepLinks = true
        showSteps = true

        add(CompanyDetailsWizardView::class)
        add(InvoiceDetailsWizardView::class)
    }

}
