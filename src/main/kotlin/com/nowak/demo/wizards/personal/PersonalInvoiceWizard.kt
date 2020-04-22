package com.nowak.demo.wizards.personal

import tornadofx.*

const val SUBTITLE= "After filling all required field new invoice will be generated and email will be send to receiver"

class PersonalInvoiceWizard : Wizard("Create new personal invoice", SUBTITLE) {
    override val canGoNext = currentPageComplete
    override val canFinish = allPagesComplete

    init {
        enableStepLinks = true
        showSteps = true
        numberedSteps = true
        finishButtonTextProperty.value = "Create"

        add(CustomerDetailsWizardView::class)
        add(PersonalInvoiceDetailsWizardView::class)

    }
}
