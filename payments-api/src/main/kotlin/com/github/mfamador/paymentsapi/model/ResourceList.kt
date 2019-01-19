package com.github.mfamador.paymentsapi.model

data class ResourceList(val data: List<Payment>, val links: Links) {
    data class Links(val self: String)
}