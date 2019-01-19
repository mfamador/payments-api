package com.github.mfamador.paymentsapi.model

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import java.util.UUID

data class Payment(
    @Id val id: String?,
    val version: Int,
    @JsonProperty("organisation_id") val organizationId: UUID,
    val attributes: Attributes) : Resource {

    constructor(id: String, p: Payment) : this(id, p.version, p.organizationId, p.attributes)

    data class Attributes(
        val amount: Double,
        @JsonProperty("beneficiary_party") val beneficiaryParty: Party,
        @JsonProperty("charges_information") val chargesInformation: ChargeInformation,
        val currency: String,
        @JsonProperty("debtor_party") val debtorParty: Party,
        @JsonProperty("end_to_end_reference") val endToEndReference: String,
        val fx: Fx,
        @JsonProperty("numeric_reference") val numericReference: String,
        @JsonProperty("payment_id") val paymentId: String,
        @JsonProperty("payment_purpose") val paymentPurpose: String,
        @JsonProperty("payment_scheme") val paymentScheme: String,
        @JsonProperty("payment_type") val paymentType: String,
        @JsonProperty("processing_date") val processingDate: String,
        val reference: String,
        @JsonProperty("scheme_payment_sub_type") val schemePaymentSubType: String,
        @JsonProperty("scheme_payment_type") val schemePaymentType: String,
        @JsonProperty("sponsor_party") val sponsorParty: Party
    )

    data class Party(
        @JsonProperty("account_name") val accountName: String?,
        @JsonProperty("account_type") val accountType: String?,
        @JsonProperty("account_number") val accountNumber: String,
        @JsonProperty("account_number_code") val accountNumberCode: String?,
        val address: String?,
        @JsonProperty("bank_id") val bankId: String,
        @JsonProperty("bank_id_code") val bankIdCode: String,
        val name: String?
    )

    data class Fx(
        @JsonProperty("contract_reference") val contractReferece: String,
        @JsonProperty("exchange_rate") val exchangeRate: String,
        @JsonProperty("original_amount") val originalAmount: String,
        @JsonProperty("original_currency") val originalCurrency: String
    )

    data class ChargeInformation(
        @JsonProperty("bearer_code") val bearerCode: String,
        @JsonProperty("sender_charges") val senderCharges: List<Charge>,
        @JsonProperty("receiver_charges_amount") val receiverChargesAmount: Double,
        @JsonProperty("receiver_charges_currency") val receiverChargesCurrency: String
    )

    data class Charge(
        val amount: Double,
        val currency: String
    )

    // TODO - figure out how to make PropertyNamingStrategy.SNAKE_CASE work with Webflux and remove all @JsonProperty
}
