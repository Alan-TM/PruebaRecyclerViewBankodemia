package mx.alan.pruebarecyclerviewbankodemia.model

data class TransactionEntity(
    val amount: Double,
    val type: String,
    val concept: String,
    val createdAt: String,
    val issuer: String,
    val destinationUser: String,
    val isIncome: Boolean
)
