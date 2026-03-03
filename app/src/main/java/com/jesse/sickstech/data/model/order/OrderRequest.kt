package com.jesse.sickstech.data.model.order

import com.google.gson.annotations.SerializedName

data class OrderRequest(
    val type: String = "point",
    @SerializedName("external_reference")
    val externalReference: String,
    val transactions: Transactions,
    val config: Config
)

data class Transactions(
    val payments: List<Payment>

)


data class Payment(
    val amount: String ,// precisa ser string com 2 casas decimais
)

data class Config(
    val point: PointConfig,

    @SerializedName("payment_method")
    val paymentMethod: PaymentMethodConfig? = null
)

data class PaymentMethodConfig(

    @SerializedName("default_type")
    val defaultType: String,

    @SerializedName("default_installments")
    val defaultInstallments: Int? = null,

    @SerializedName("installments_cost")
    val installmentsCost: String? = null
)


data class PointConfig(
    @SerializedName("terminal_id")
    val terminalId: String,
    @SerializedName("print_on_terminal")
    val printOnTerminal: String = "no_ticket"
)
