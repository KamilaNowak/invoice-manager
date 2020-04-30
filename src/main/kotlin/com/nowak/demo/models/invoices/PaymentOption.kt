package com.nowak.demo.models.invoices

enum class PaymentMethod(s: String ="CREDIT_CARD") {
    DEBIT_CARD("DEBIT_CARD"),
    CREDIT_CARD("CREDIT_CARD"),
    IDEAL("IDEAL"),
    CASH("CASH"),
    PAYPAL("PAYPAL"),
    BLIK("BLIK"),
    CRYPTOCURRENCY("CRYPTOCURRENCY")
}