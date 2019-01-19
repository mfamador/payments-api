package com.github.mfamador.paymentsapi.model

data class PaymentList (val data: List<Payment>,
                        val links: Links
) {
    data class Links(val self: String)
}