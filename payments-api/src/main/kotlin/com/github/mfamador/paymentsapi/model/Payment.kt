package com.github.mfamador.paymentsapi.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import org.springframework.data.annotation.Id
import java.util.*

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(JsonSubTypes.Type(Payment::class))
interface Operation

data class Payment(@Id val id: String?,
                   val version: Int,
                   @JsonProperty("organisation_id") val organizationId: UUID,
                   val attributes: Attributes,
                   val description: String) : Operation {

    constructor(id: String, p: Payment) : this(id, p.version, p.organizationId, p.attributes, p.description)
}

data class Attributes(val amount: Double,
                      @JsonProperty("beneficiary_party") val beneficiaryParty: Party)

data class Party(@JsonProperty("account_name") val accountName: String)